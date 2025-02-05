package com.example.backend.global.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

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

	public Long getAccessTokenExpirationTime() {
		return ACCESS_TOKEN_EXPIRATION_TIME;
	}

	public Long getRefreshTokenExpirationTime() {
		return REFRESH_TOKEN_EXPIRATION_TIME;
	}

	private Key key;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());  // `@Value` 주입된 후 실행됨!
	}

	public Key getKey() {
		return key;
	}

	// 토큰 검증 메서드
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// JWT 에서 사용자 정보 추출
	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();

		String adminName = claims.getSubject();
		List<GrantedAuthority> authority = new ArrayList<>();
		authority.add(getAuthoritiesFromClaims(claims));

		return new UsernamePasswordAuthenticationToken(adminName, "", authority);
	}

	// Claims 에서 권한을 추출하는 메서드
	private GrantedAuthority getAuthoritiesFromClaims(Claims claims) {
		String role = claims.get("role", String.class);

		return new SimpleGrantedAuthority(role);
	}

	// JWT 검증 및 정보 추출
	public Claims parseToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	// JWT 에서 유저 정보 가져오기
	public String getUserName(String token) {
		return parseToken(token).getSubject();
	}

	public String getAdminRole(String token) {
		return parseToken(token).get("role", String.class);
	}

	// 리프레시 토큰 유효성 검사
	public boolean isRefreshTokenValid(String refreshToken) {
		try {
			Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(refreshToken);

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
