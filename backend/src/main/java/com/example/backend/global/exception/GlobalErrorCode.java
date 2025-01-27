package com.example.backend.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * GlobalErrorCode
 * 전역에서 발생 할 수 있는 커스텀 예외 정리 클래스
 * @author 100minha
 */
@AllArgsConstructor
@Getter
public enum GlobalErrorCode {
	NOT_VALID(HttpStatus.BAD_REQUEST, "400-1", "입력된 객체가 유효하지 않습니다"),
	NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 그룹은 존재하지 않습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

}
