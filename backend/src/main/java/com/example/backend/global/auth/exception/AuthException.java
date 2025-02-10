package com.example.backend.global.auth.exception;

import org.springframework.http.HttpStatus;

/**
 * AuthException
 * <p></p>
 * @author 100mina
 */
public class AuthException extends RuntimeException {
	private final AuthErrorCode authErrorCode;

	public AuthException(AuthErrorCode authErrorCode) {
		super(authErrorCode.getMessage());
		this.authErrorCode = authErrorCode;
	}

	public HttpStatus getStatus() {
		return authErrorCode.getHttpStatus();
	}

	public String getCode() {
		return authErrorCode.getCode();
	}
}