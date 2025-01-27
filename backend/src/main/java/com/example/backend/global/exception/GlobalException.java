package com.example.backend.global.exception;

import org.springframework.http.HttpStatus;

/**
 * GlobalException
 * 전역에서 발생할 수 있는 커스텀 예외 처리 클래스
 * @author 100minha
 */
public class GlobalException extends RuntimeException {
	private final GlobalErrorCode globalErrorCode;

	public GlobalException(GlobalErrorCode globalErrorCode) {
		super(globalErrorCode.getMessage());
		this.globalErrorCode = globalErrorCode;
	}

	public HttpStatus getStatus() {
		return globalErrorCode.getHttpStatus();
	}

	public String getCode() {
		return globalErrorCode.getCode();
	}
}
