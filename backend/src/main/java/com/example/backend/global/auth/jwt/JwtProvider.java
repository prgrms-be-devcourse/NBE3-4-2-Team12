package com.example.backend.global.auth.jwt;

import com.example.backend.domain.admin.entity.Admin;
import com.example.backend.global.auth.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtProvider {

	private final JwtUtil jwtUtil;

	private SecretKey key; // 초기에는 null

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(jwtUtil.getSecretKey().getBytes());  // `@Value` 주입된 후 실행됨!
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

	// JWT 생성
	public String generateToken(Admin admin) {
		return Jwts.builder()
			.setSubject(admin.getId().toString())  // 사용자 이름 (관리자 ID)
			.claim("role", admin.getRole())  // 역할 (권한 정보)
			.setIssuedAt(new Date())  // 토큰 발급 시간
			.setExpiration(new Date(System.currentTimeMillis() + jwtUtil.getAccessTokenExpirationTime()))  // 만료시간
			.signWith(key)  // 서명 알고리즘 및 키
			.compact();
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
	public String getUsername(String token) {
		return parseToken(token).getSubject();
	}

	public String getUserRole(String token) {
		return parseToken(token).get("role", String.class);
	}

	// 리프레시 토큰 생성
	public String generateRefreshToken(Admin admin) {
        return UUID.randomUUID().toString();
	}


}
