package com.example.backend.global.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.backend.global.auth.util.JwtUtil;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * TestTokenProvider
 * <p></p>
 * @author 100mi
 */
@Component
@Profile("test")
public class TestTokenProvider {

	@Autowired
	private JwtUtil jwtUtil;

	public String generateMemberAccessToken(Long id, String nickname, String email) {
		return Jwts.builder()
			.setSubject(nickname)  // 사용자 nickname
			.claim("id", id)    // Member primary-key
			.claim("email", email)
			.setIssuedAt(new Date())  // 토큰 발급 시간
			.setExpiration(new Date(System.currentTimeMillis() + 10_000))  // 테스트용으로만 사용될 토큰이므로 보안상 만료 시간 10초로 설정
			.signWith(jwtUtil.getKey(), SignatureAlgorithm.HS256)  // 서명 알고리즘 및 키
			.compact();
	}
}
