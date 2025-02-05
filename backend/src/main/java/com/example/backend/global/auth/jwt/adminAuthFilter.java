package com.example.backend.global.auth.jwt;


import com.example.backend.global.auth.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class adminAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws ServletException, IOException {

        // 요청 헤더에서 JWT 토큰 가져오기
        String token = resolveToken(request);
        String refreshToken = request.getHeader("Refresh-Token");

        // 토큰이 유효하면 SecurityContext 에 인증 정보 저장
        if(token != null && jwtUtil.validateToken(token)) {
            Authentication authentication = jwtUtil.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (token != null && jwtUtil.isRefreshTokenValid(refreshToken)) {

        }

        // 다음 필터에 요청 전달
        chain.doFilter(request, response);
    }

    // 요청 헤더에서 Authorization 값 가져오기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
