package com.example.backend.global.auth.jwt;


import com.example.backend.domain.admin.entity.Admin;
import com.example.backend.domain.admin.repository.AdminRepository;
import com.example.backend.global.auth.util.JwtUtil;
import com.example.backend.global.auth.util.TokenProvider;
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
public class AdminAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenProvider tokenProvider;
    private final AdminRepository adminRepository;
    private final CookieService cookieService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws ServletException, IOException {

        String path = request.getServletPath();

        // 로그인 요청은 필터 통과
        if (path.equals("/admin/login")) {
            chain.doFilter(request, response);
            return;
        }

        // 요청 헤더에서 JWT 토큰 가져오기
        String accessToken = this.cookieService.getAccessTokenFromCookie(request);

        // 토큰이 유효하면 SecurityContext 에 인증 정보 저장
        if (accessToken == null) {
            chain.doFilter(request, response);
            return;
        }

        if (jwtUtil.validateToken(accessToken)) {
            Authentication authentication = jwtUtil.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
            return;
        } else if (jwtUtil.isTokenExpired(accessToken)) {
            String refreshToken = this.cookieService.getRefreshTokenFromCookie(request);

            if (refreshToken != null && jwtUtil.isRefreshTokenValid(refreshToken)) {
                Admin admin = adminRepository.findByRefreshToken(refreshToken);
                String newAccessToken = this.tokenProvider.generateToken(admin);
                this.cookieService.addAccessTokenToCookie(newAccessToken, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired refresh token");
                return;
            }
        }

        // 다음 필터에 요청 전달
        chain.doFilter(request, response);
    }

}
