package com.example.backend.global.auth.kakao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.global.auth.kakao.dto.KakaoTokenResponseDto;
import com.example.backend.global.auth.kakao.dto.KakaoUserInfoResponseDto;
import com.example.backend.global.auth.kakao.service.KakaoAuthService;
import com.example.backend.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * KakaoAuthController
 * 사용자 인증 관련 controller입니다
 * @author 100minha
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth/kakao")
@RestController
public class KakaoAuthController {

	private final KakaoAuthService kakaoAuthService;

	/**
	 * 카카오 로그인 페이지로 리다이렉트 및
	 * 인가 토큰 발급 요청하는 메서드
	 * @return
	 */
	@GetMapping("/login")
	public ResponseEntity<Void> kakaoLogin() {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", kakaoAuthService.getKakaoAuthorizationUrl());

		return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
	}

	/**
	 * 카카오에서 발급 받은 인가 토큰으로 access토큰, refresh토큰 요청하는 메서드
	 * @param authorizationCode
	 * @return
	 */
	@GetMapping("/callback")
	public ResponseEntity<Object> getTokenFromKakao(
		@RequestParam(value = "code", required = false) String authorizationCode,
		@RequestParam(value = "error", required = false) String error,
		@RequestParam(value = "error-description", required = false) String errorDescription) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "https://localhost:3000");

		// 카카오에서 인가 토큰이 아닌 에러를 반환할 시 홈페이지로 리다이렉트 및 에러 메세지 응답
		if (error != null) {

			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", error);
			errorResponse.put("error-description", errorDescription);

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers)
				.body(errorResponse);
		}

		KakaoTokenResponseDto kakaoTokenDto = kakaoAuthService.getTokenFromKakao(authorizationCode);
		KakaoUserInfoResponseDto kakaoUserInfoDto = kakaoAuthService.getUserInfo(kakaoTokenDto.accessToken());

		if (!kakaoAuthService.existsMemberByKakaoId(kakaoUserInfoDto.id())) {
			kakaoAuthService.join(kakaoUserInfoDto, kakaoTokenDto.refreshToken());
		}

		//TODO: 쿠키에 엑세스토큰 담는 로직 필요

		return ResponseEntity.ok()
			.headers(headers)
			.body(ApiResponse.of("성공적으로 로그인 되었습니다."));
	}

}
