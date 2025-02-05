package com.example.backend.domain.member.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * MemberErrorCode
 * 도메인 Member 커스텀 에러
 * @author 100minha
 */
@AllArgsConstructor
@Getter
public enum MemberErrorCode {

	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "사용자를 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
