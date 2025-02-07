package com.example.backend.global.auth.jwt;


import com.example.backend.domain.admin.entity.Admin;
import com.example.backend.domain.admin.repository.AdminRepository;
import com.example.backend.global.auth.service.CookieService;
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

        // 요청 헤더에서 JWT 토큰 가져오기
        String accessToken = this.cookieService.getAccessTokenFromCookie(request);
        String refreshToken = this.cookieService.getRefreshTokenFromCookie(request);

        // 토큰이 유효하면 SecurityContext 에 인증 정보 저장
        if (accessToken == null) {
            if (refreshToken == null) {
                chain.doFilter(request, response);
                return;
            }
            return;
        }

        TokenStatus tokenStatus = jwtUtil.validateToken(accessToken);

        switch (tokenStatus) {
            case VALID:
                Authentication authentication = jwtUtil.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
                break;
            case EXPIRED:
                    if (jwtUtil.isRefreshTokenValid(refreshToken)) {
                        Admin admin = adminRepository.findByRefreshToken(refreshToken);
                        String newAccessToken = this.tokenProvider.generateToken(admin);
                        this.cookieService.addAccessTokenToCookie(newAccessToken, response);
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("토큰이 만료되었습니다.");
                        return;
                    }
                break;
            case MALFORMED:
                System.out.println("잘못된 형식의 토큰입니다.");
                break;
            case INVALID:
                System.out.println("토큰이 비어있거나 올바르지 않습니다.");
                break;
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        return !((method.equals("POST") && path.startsWith("/admin/")) ||
                (method.equals("GET") && path.startsWith("/admin/")) ||
                (method.equals("DELETE") && path.startsWith("/admin/")));
    }
}
