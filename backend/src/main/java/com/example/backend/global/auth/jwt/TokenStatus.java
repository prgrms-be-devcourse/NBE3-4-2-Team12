package com.example.backend.global.auth.jwt;

/**
 * TokenStatus
 * 액세스 토큰 검증 결과를 구분하는 Enum 클래스
 * @author 100minha
 */
public enum TokenStatus {
	VALID,         // 유효한 토큰
	EXPIRED,       // 만료된 토큰
	MALFORMED,     // 잘못된 형식의 토큰
	INVALID;       // 비어있거나 유효하지 않은 토큰
}
