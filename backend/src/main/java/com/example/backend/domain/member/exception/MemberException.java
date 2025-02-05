package com.example.backend.domain.member.exception;

import org.springframework.http.HttpStatus;

/**
 * MemberException
 * <p></p>
 * @author 100mi
 */
public class MemberException extends RuntimeException {
	private final MemberErrorCode memberErrorCode;

	public MemberException(MemberErrorCode memberErrorCode) {
		super(memberErrorCode.name());
		this.memberErrorCode = memberErrorCode;
	}

	public HttpStatus getStatus() {
		return memberErrorCode.getHttpStatus();
	}

	public String getCode() {
		return memberErrorCode.getCode();
	}
}
