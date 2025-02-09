package com.example.backend.global.auth.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AuthErrorCode
 * <p></p>
 * @author 100minha
 */
@AllArgsConstructor
@Getter
public enum AuthErrorCode {
	AUTHORIZATION_FAILED(HttpStatus.UNAUTHORIZED, "401-1", "인증에 실패했습니다."),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "401-2", "토큰이 만료되었습니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "401-3", "유효하지 않은 토큰입니다."),
	TOKEN_REISSUE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "500", "토큰 갱신에 실패했습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}