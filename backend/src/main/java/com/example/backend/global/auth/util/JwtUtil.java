package com.example.backend.global.auth.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JwtUtil
 * jwt 관련 유틸 클래스
 * @author 100minha
 */
@Component
public class JwtUtil {

	@Value("${spring.security.jwt.secret-key}")
	private String SECRET_KEY;

	@Value("${spring.security.jwt.access-token.expiration}")
	private long ACCESS_TOKEN_EXPIRATION_TIME; // 6시간 (단위: ms)

	@Value("${spring.security.jwt.refresh-token.expiration}")
	private long REFRESH_TOKEN_EXPIRATION_TIME; // 60일(약 2달) (단위: ms)

	public String getSecretKey() {
		return SECRET_KEY;
	}

	public Long getAccessTokenExpirationTime() {
		return ACCESS_TOKEN_EXPIRATION_TIME;
	}

	public Long getRefreshTokenExpirationTime() {
		return REFRESH_TOKEN_EXPIRATION_TIME;
	}
}
