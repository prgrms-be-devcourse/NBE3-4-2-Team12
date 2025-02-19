package com.example.backend.global.auth.kakao.exception;

import org.springframework.http.HttpStatus;

/**
 * KakaoAuthException
 * 카카오와 통신 도중 발생할 수 있는 예외 처리
 * @author 100minha
 */
public class KakaoAuthException extends RuntimeException {
	private final KakaoAuthErrorCode kakaoAuthErrorCode;

	public KakaoAuthException(KakaoAuthErrorCode kakaoAuthErrorCode) {
		super(kakaoAuthErrorCode.getMessage());
		this.kakaoAuthErrorCode = kakaoAuthErrorCode;
	}

	public HttpStatus getStatus() {
		return kakaoAuthErrorCode.getHttpStatus();
	}

	public String getCode() {
		return kakaoAuthErrorCode.getCode();
	}
}
