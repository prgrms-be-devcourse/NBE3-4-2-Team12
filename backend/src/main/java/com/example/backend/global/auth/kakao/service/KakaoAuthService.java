package com.example.backend.global.auth.kakao.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.backend.domain.member.dto.MemberTokenReissueDto;
import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.service.MemberService;
import com.example.backend.global.auth.kakao.dto.KakaoTokenResponseDto;
import com.example.backend.global.auth.kakao.dto.KakaoUserInfoResponseDto;
import com.example.backend.global.auth.kakao.dto.LoginResponseDto;
import com.example.backend.global.auth.kakao.exception.KakaoAuthErrorCode;
import com.example.backend.global.auth.kakao.exception.KakaoAuthException;
import com.example.backend.global.auth.kakao.util.KakaoAuthUtil;
import com.example.backend.global.auth.service.CookieService;
import com.example.backend.global.auth.util.TokenProvider;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * KakaoAuthService
 * <p></p>
 * @author 100minha
 */
@RequiredArgsConstructor
@Service
public class KakaoAuthService {

	private final KakaoAuthUtil kakaoAuthUtil;
	private final WebClient webClient;
	private final TokenProvider tokenProvider;
	private final MemberService memberService;
	private final CookieService cookieService;

	public String getKakaoAuthorizationUrl() {
		return kakaoAuthUtil.getKakaoAuthorizationUrl();
	}

	/**
	 * 카카오 서버에 엑세스 토큰, 리프레시 토큰 발급을 요청하는 메서드
	 * @param authorizationCode
	 * @return
	 */
	public KakaoTokenResponseDto getTokenFromKakao(String authorizationCode) {

		KakaoTokenResponseDto kakaoTokenResponseDto = webClient.post()
			.uri(kakaoAuthUtil.getKakaoLoginTokenUrl(authorizationCode))
			.retrieve()
			//TODO : 더 상세한 예외 처리 필요
			.onStatus(HttpStatusCode::is4xxClientError,
				clientResponse -> Mono.error(new KakaoAuthException(KakaoAuthErrorCode.INVALID_PARAMETER)))
			.onStatus(HttpStatusCode::is5xxServerError,
				clientResponse -> Mono.error(new KakaoAuthException(KakaoAuthErrorCode.KAKAO_SERVER_ERROR)))
			.bodyToMono(KakaoTokenResponseDto.class)
			.block();

		return kakaoTokenResponseDto;
	}

	/**
	 * 카카오 서버에 엑세스 토큰을 사용하여 사용자 정보를 요청하는 메서드
	 * @param accessToken
	 * @return
	 */
	public KakaoUserInfoResponseDto getUserInfo(String accessToken) {

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.valueOf("application/x-www-form-urlencoded;charset=utf-8"));

		KakaoUserInfoResponseDto kakaoUserInfoDto = webClient.get()
			.uri(kakaoAuthUtil.getUserInfoUrl())
			.headers(httpHeaders -> httpHeaders.addAll(headers))
			.retrieve()
			//TODO : 더 상세한 예외 처리 필요
			.onStatus(HttpStatusCode::is4xxClientError,
				clientResponse -> Mono.error(new KakaoAuthException(KakaoAuthErrorCode.INVALID_PARAMETER)))
			.onStatus(HttpStatusCode::is5xxServerError,
				clientResponse -> Mono.error(new KakaoAuthException(KakaoAuthErrorCode.KAKAO_SERVER_ERROR)))
			.bodyToMono(KakaoUserInfoResponseDto.class)
			.block();

		return kakaoUserInfoDto;
	}

	@Transactional
	public LoginResponseDto login(Long kakaoId, KakaoTokenResponseDto kakaoTokenDto) {

		Member member = memberService.findByKakaoId(kakaoId);
		member.updateAccessToken(kakaoTokenDto.accessToken());
		member.updateRefreshToken(kakaoTokenDto.refreshToken());

		String accessToken = tokenProvider.generateMemberAccessToken(
			member.getId(), member.getNickname(), member.getEmail());

		return LoginResponseDto.builder()
			.nickname(member.getNickname())
			.accessToken(accessToken)
			.refreshToken(kakaoTokenDto.refreshToken())
			.build();
	}

	public String getKakaoLogoutUrl(Long userId) {

		return kakaoAuthUtil.getLogoutUrl(userId);
	}

	public boolean existsMemberByKakaoId(Long kakaoId) {
		return memberService.existsByKakaoId(kakaoId);
	}

	public void join(KakaoUserInfoResponseDto kakaoUserInfoDto) {
		memberService.join(kakaoUserInfoDto);
	}

	@Transactional
	public void logout(Long userId, HttpServletResponse response) {

		cookieService.clearTokenFromCookie(response);
		Member member = memberService.findById(userId);
		member.updateAccessToken(null);
		member.updateRefreshToken(null);

		SecurityContextHolder.clearContext();
	}

	@Transactional
	public MemberTokenReissueDto reissueTokens(String refreshToken) {

		Member member = memberService.findByKakaoRefreshToken(refreshToken);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf("application/x-www-form-urlencoded;charset=utf-8"));

		KakaoTokenResponseDto kakaoTokenDto = webClient.post()
			.uri(kakaoAuthUtil.getKakaoTokenReissueUrl(refreshToken))
			.headers(httpHeaders -> httpHeaders.addAll(headers))
			.retrieve()
			//TODO : 더 상세한 예외 처리 필요
			.onStatus(HttpStatusCode::is4xxClientError,
				clientResponse -> Mono.error(new KakaoAuthException(KakaoAuthErrorCode.INVALID_PARAMETER)))
			.onStatus(HttpStatusCode::is5xxServerError,
				clientResponse -> Mono.error(new KakaoAuthException(KakaoAuthErrorCode.KAKAO_SERVER_ERROR)))
			.bodyToMono(KakaoTokenResponseDto.class)
			.block();

		// dto에 토큰정보가 담겨있지 않을 시 예외 반환 및 리프레시 토큰은 body에 있을 경우에만 갱신
		if (kakaoTokenDto != null) {

			member.updateAccessToken(kakaoTokenDto.accessToken());
			if (kakaoTokenDto.refreshToken() != null) {
				member.updateRefreshToken(kakaoTokenDto.refreshToken());
			}
		} else {
			throw new KakaoAuthException(KakaoAuthErrorCode.TOKEN_REISSUE_FAILED);
		}

		return MemberTokenReissueDto.of(member);
	}
}
