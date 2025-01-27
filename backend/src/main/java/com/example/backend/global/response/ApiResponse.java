package com.example.backend.global.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * ApiResponse
 * 공통 응답 객체
 * @author 100minha
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

	private String message;  // 응답 메시지
	private T data;          // 응답 데이터

	// 메시지만 포함하는 응답
	public static <T> ApiResponse<T> of(String message) {
		return ApiResponse.<T>builder()
			.message(message)
			.build();
	}

	// 데이터만 포함하는 응답
	public static <T> ApiResponse<T> of(T data) {
		return ApiResponse.<T>builder()
			.data(data)
			.build();
	}

	// 메시지와 데이터를 포함하는 응답
	public static <T> ApiResponse<T> of(String message, T data) {
		return ApiResponse.<T>builder()
			.message(message)
			.data(data)
			.build();
	}
}
