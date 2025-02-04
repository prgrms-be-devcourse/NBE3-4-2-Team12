package com.example.backend.global.advice;

import com.example.backend.domain.admin.exception.AdminException;
import com.example.backend.domain.group.exception.GroupException;
import com.example.backend.global.exception.GlobalErrorCode;
import com.example.backend.global.exception.GlobalException;
import com.example.backend.global.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

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
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
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
				request.getRequestURI(),
				errors
			)
		);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
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
				request.getRequestURI(),
				errors
			)
		);
	}

	@ExceptionHandler(GroupException.class)
	public ResponseEntity<ErrorResponse> handleGroupException(GroupException ex, HttpServletRequest request) {
		return ResponseEntity.status(ex.getStatus())
				.body(ErrorResponse.of(ex.getMessage(), ex.getCode(), request.getRequestURI()));
	}

	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(GlobalException ex, HttpServletRequest request) {
		return ResponseEntity.status(ex.getStatus())
				.body(ErrorResponse.of(ex.getMessage(), ex.getCode(), request.getRequestURI()));
	}

	@ExceptionHandler(AdminException.class)
	public ResponseEntity<ErrorResponse> handleAdminException(AdminException ex, HttpServletRequest request) {
		return ResponseEntity.status(ex.getStatus())
				.body(ErrorResponse.of(ex.getMessage(), ex.getCode(), request.getRequestURI()));
	}

	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<ErrorResponse> handleCategoryException(GlobalException ex, HttpServletRequest request) {
		return ResponseEntity.status(ex.getStatus())
				.body(ErrorResponse.of(ex.getMessage(), ex.getCode(), request.getRequestURI()));
	}
}
