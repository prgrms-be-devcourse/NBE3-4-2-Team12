package com.example.backend.global.auth.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.global.auth.dto.KakaoTokenResponseDto;
import com.example.backend.global.auth.dto.KakaoUserInfoResponseDto;
import com.example.backend.global.auth.dto.LoginResponseDto;
import com.example.backend.global.auth.model.CustomUserDetails;
import com.example.backend.global.auth.service.CookieService;
import com.example.backend.global.auth.service.KakaoAuthService;
import com.example.backend.global.response.ApiResponse;
import com.example.backend.global.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * KakaoAuthController
 * 사용자 인증 관련 controller입니다
 * @author 100minha
 */
@Slf4j
@RequestMapping("/auth/kakao")
@RestController
public class KakaoAuthController {

	private final KakaoAuthService kakaoAuthService;
	private final CookieService cookieService;
	private final String clientBaseUrl;

	public KakaoAuthController(KakaoAuthService kakaoAuthService, CookieService cookieService,
		@Value("${CLIENT_BASE_URL}") String clientBaseUrl
	) {
		this.kakaoAuthService = kakaoAuthService;
		this.cookieService = cookieService;
		this.clientBaseUrl = clientBaseUrl;
	}

	/**
	 * 카카오 로그인 페이지로 리다이렉트 및
	 * 인가 토큰 발급 요청하는 메서드
	 * @return
	 */
	@GetMapping("/login")
	public ResponseEntity<Object> kakaoLogin() {

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(kakaoAuthService.getKakaoAuthorizationUrl()));

		return ResponseEntity.status(HttpStatus.FOUND).headers(headers).body(null);
	}

	/**
	 * 카카오에서 발급 받은 인가 토큰으로 access토큰, refresh토큰 요청하는 메서드
	 * @param authorizationCode
	 * @return
	 */
	@GetMapping("/callback")
	public ResponseEntity<Object> authorizeAndLoginWithKakao(
		@RequestParam(value = "code", required = false) String authorizationCode,
		@RequestParam(value = "error", required = false) String error,
		@RequestParam(value = "error-description", required = false) String errorDescription,
		HttpServletRequest request, HttpServletResponse response) {

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(clientBaseUrl));

		// 카카오에서 인가 토큰이 아닌 에러를 반환할 시 홈페이지로 리다이렉트 및 에러 메세지 응답
		if (error != null) {
			ErrorResponse errorResponse = ErrorResponse.of(
				errorDescription, "400-" + error, request.getRequestURI());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers)
				.body(errorResponse);
		}

		KakaoTokenResponseDto kakaoTokenDto = kakaoAuthService.getTokenFromKakao(authorizationCode);
		KakaoUserInfoResponseDto kakaoUserInfoDto = kakaoAuthService.getUserInfo(kakaoTokenDto.accessToken());

		Long kakaoId = kakaoUserInfoDto.id();

		// 기존 사용인지 검증 후 신규 사용자일 시 회원가입 진행
		checkNewMemberAndJoin(kakaoId, kakaoUserInfoDto);

		// 로그인 진행
		return login(kakaoTokenDto, kakaoId, response, headers);
	}

	private void checkNewMemberAndJoin(Long kakaoId, KakaoUserInfoResponseDto kakaoUserInfoDto) {
		if (!kakaoAuthService.existsMemberByKakaoId(kakaoId)) {
			kakaoAuthService.join(kakaoUserInfoDto);
		}
	}

	private ResponseEntity<Object> login(
		KakaoTokenResponseDto kakaoTokenDto, Long kakaoId,
		HttpServletResponse response, HttpHeaders headers
	) {
		LoginResponseDto loginDto = kakaoAuthService.login(kakaoId, kakaoTokenDto);

		cookieService.addAccessTokenToCookie(loginDto.accessToken(), response);
		cookieService.addRefreshTokenToCookie(loginDto.refreshToken(), response);

		return ResponseEntity.status(HttpStatus.FOUND)
			.headers(headers)
			.body(ApiResponse.of("성공적으로 로그인 되었습니다. nickname : " + loginDto.nickname()));
	}

	/**
	 * 카카오 로그아웃 옵션 선택 페이지로 리다이렉트
	 * @param response
	 * @param customUserDetails
	 * @return
	 */
	@GetMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletResponse response,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(kakaoAuthService.getKakaoLogoutUrl(customUserDetails.getUserId())));

		return ResponseEntity.status(HttpStatus.FOUND)
			.headers(headers).body(null);
	}

	@GetMapping("/logout/callback")
	public ResponseEntity<ApiResponse<String>> handleKakaoLogoutCallback(HttpServletResponse response,
		@RequestParam("state") Long userId) {

		kakaoAuthService.logout(userId, response);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(clientBaseUrl));

		return ResponseEntity.status(HttpStatus.FOUND)
			.headers(headers).body(ApiResponse.of("성공적으로 로그아웃 되었습니다."));
	}

}
