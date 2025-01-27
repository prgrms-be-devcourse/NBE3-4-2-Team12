package com.example.backend.global.advice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.backend.global.exception.GlobalErrorCode;
import com.example.backend.global.response.ErrorResponse;

import jakarta.validation.ConstraintViolationException;

/**
 * GlobalControllerAdvice
 * Global 예외 처리 advice 클래스
 * @author 100minha
 */
@ControllerAdvice
public class GlobalControllerAdvice {

	/**
	 * 컨트롤러 @Valid 검증 실패 예외 핸들러 메소드
	 * 컨트롤러 레이어에서
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		List<ErrorResponse.ValidationError> errors = new ArrayList<>();

		ex.getBindingResult().getAllErrors().forEach(error -> {
			errors.add(ErrorResponse.ValidationError.builder()
				.field(((FieldError)error).getField())
				.message(error.getDefaultMessage())
				.build());
		});

		return ResponseEntity.status(ex.getStatusCode().value()).body(
			ErrorResponse.of(
				GlobalErrorCode.NOT_VALID.getMessage(),
				GlobalErrorCode.NOT_VALID.getCode(),
				errors
			)
		);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
		List<ErrorResponse.ValidationError> errors = new ArrayList<>();

		ex.getConstraintViolations().forEach(violation -> {
			String fieldName = violation.getPropertyPath().toString();
			String message = violation.getMessage();
			errors.add(new ErrorResponse.ValidationError(fieldName, message));
		});

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
			ErrorResponse.of(
				GlobalErrorCode.NOT_VALID.getMessage(),
				GlobalErrorCode.NOT_VALID.getCode(),
				errors
			)
		);
	}

}
