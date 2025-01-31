package com.example.backend.global.config;

import com.example.backend.domain.admin.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final CustomUserDetailsService customUserDetailsService;
    private final String SECRET_KEY = "q_W?DNW;gK\"[S8k}q{J>!8lDvH<SL#p8E;f&SA7:^p9rJPQ\"`CaSM_vfyynbq8I";  // 실제 환경에서 환경변수로 설정
    private final long EXPIRATION_TIME = 1000L * 60 * 60;  // 1시간 (단위: ms)
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());  // 키 생성

    // 토큰 검증 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
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
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    // JWT 생성
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)  // 사용자 이름 (관리자 ID)
                .claim("role", role)  // 역할 (권한 정보)
                .setIssuedAt(new Date())  // 토큰 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // 만료시간
                .signWith(key)  // 서명 알고리즘 및 키
                .compact();
    }

    // JWT 검증 및 정보 추출
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // JWT에서 유저 정보 가져오기
    public String getUsername(String token) {
        return parseToken(token).getSubject();
    }

    public String getUserRole(String token) {
        return parseToken(token).get("role", String.class);
    }
}
