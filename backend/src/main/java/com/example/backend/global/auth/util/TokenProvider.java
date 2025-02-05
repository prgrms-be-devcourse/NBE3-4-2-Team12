package com.example.backend.global.auth.util;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.backend.domain.admin.entity.Admin;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenProvider {

	private final JwtUtil jwtUtil;

	// JWT 생성
	public String generateToken(Admin admin) {
		return Jwts.builder()
			.setSubject(admin.getId().toString())  // 사용자 이름 (관리자 ID)
			.claim("role", admin.getRole())  // 역할 (권한 정보)
			.setIssuedAt(new Date())  // 토큰 발급 시간
			.setExpiration(new Date(System.currentTimeMillis() + jwtUtil.getAccessTokenExpirationTime()))  // 만료시간
			.signWith(jwtUtil.getKey())  // 서명 알고리즘 및 키
			.compact();
	}

	public String generateMemberAccessToken(Long id, String nickname, String email) {
		return Jwts.builder()
			.setSubject(nickname)  // 사용자 nickname
			.claim("id", id)    // Member primary-key
			.claim("email", email)
			.setIssuedAt(new Date())  // 토큰 발급 시간
			.setExpiration(new Date(System.currentTimeMillis() + jwtUtil.getAccessTokenExpirationTime()))  // 만료시간
			.signWith(jwtUtil.getKey(), SignatureAlgorithm.HS256)  // 서명 알고리즘 및 키
			.compact();
	}

	// 리프레시 토큰 생성
	public String generateRefreshToken() {
		return UUID.randomUUID().toString();
	}

	// 리프레시 토큰 만료 시간 계산
	public LocalDateTime getRefreshTokenExpiryDate() {
		return LocalDateTime.now().plusDays(jwtUtil.getRefreshTokenExpirationTime());
	}

}
