package com.example.backend.global.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

/**
 * ErrorResponse
 * 전역에서 예외 발생 시 응답떄 사용할 클래스
 * @author 100minha
 */
@Builder(access = AccessLevel.PRIVATE)
public record ErrorResponse(
	String message,
	String code,
	String requestUri,
	List<ValidationError> errors
) {

	/**
	 * Validation 예외 발생 시 필드별 예외 정보 관리 클래스
	 * @param field
	 * @param message
	 */
	@Builder
	public static record ValidationError(
		String field,    // 에러가 발생한 필드명
		String message    // 해당 필드의 에러 메시지
	) {
	}

	/**
	 * Validation 예외 발생 시 응답 때 사용할 팩토리 메서드
	 * @param message    // 예외 메세지
	 * @param code    // 예외 상태 코드
	 * @param errors    // 필드별 에러 메세지
	 */
	public static ErrorResponse of(String message, String code, String requestUri, List<ValidationError> errors) {
		return ErrorResponse.builder()
			.message(message)
			.code(code)
			.requestUri(requestUri)
			.errors(errors)
			.build();
	}

	/**
	 * 이외에 FieldError를 포함하지 않는 예외 발생 시 사용할 팩토리 메서드
	 * @param message    // 예외 메세지
	 * @param code    // 예외 상태 코드
	 */
	public static ErrorResponse of(String message, String code, String requestUri) {
		return ErrorResponse.builder()
			.message(message)
			.code(code)
			.requestUri(requestUri)
			.build();
	}

}
