package com.example.backend.global.auth.kakao.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.backend.domain.member.service.MemberService;
import com.example.backend.global.auth.kakao.dto.KakaoTokenResponseDto;
import com.example.backend.global.auth.kakao.dto.KakaoUserInfoResponseDto;
import com.example.backend.global.auth.kakao.exception.KakaoAuthErrorCode;
import com.example.backend.global.auth.kakao.exception.KakaoAuthException;
import com.example.backend.global.auth.kakao.util.KakaoAuthUtil;

import io.netty.handler.codec.http.HttpHeaderValues;
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
	private final MemberService memberService;

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
			.uri(kakaoAuthUtil.getKakaoTokenUrl(authorizationCode))
			.header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
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

	public boolean existsMemberByKakaoId(Long kakaoId) {

		return memberService.existsByKakaoId(kakaoId);
	}

	public void join(KakaoUserInfoResponseDto kakaoUserInfoDto, String refreshToken) {

		memberService.join(kakaoUserInfoDto, refreshToken);
	}
}
