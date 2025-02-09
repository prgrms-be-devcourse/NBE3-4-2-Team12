package com.example.backend.global.auth.jwt;


import com.example.backend.domain.admin.entity.Admin;
import com.example.backend.domain.admin.repository.AdminRepository;
import com.example.backend.global.auth.exception.AuthErrorCode;
import com.example.backend.global.auth.service.CookieService;
import com.example.backend.global.auth.util.JwtUtil;
import com.example.backend.global.auth.util.TokenProvider;
import com.example.backend.global.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Set;

import static com.example.backend.global.auth.exception.AuthErrorCode.*;

@Component
@RequiredArgsConstructor
public class AdminAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenProvider tokenProvider;
    private final AdminRepository adminRepository;
    private final CookieService cookieService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws ServletException, IOException {
        // 요청 헤더에서 JWT 토큰 가져오기
        String accessToken = this.cookieService.getAccessTokenFromCookie(request);
        String refreshToken = this.cookieService.getRefreshTokenFromCookie(request);

        // 토큰이 유효하면 SecurityContext 에 인증 정보 저장
        if (accessToken == null || refreshToken == null || accessToken.isEmpty() || refreshToken.isEmpty()) {
            handleException(TOKEN_EXPIRED, request, response);
        }

        try {
        TokenStatus tokenStatus = jwtUtil.validateToken(accessToken);

        switch (tokenStatus) {
            case VALID:
                Authentication authentication = jwtUtil.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
                return;
            case EXPIRED:
                    if (jwtUtil.isRefreshTokenValid(refreshToken)) {
                        Admin admin = adminRepository.findByRefreshToken(refreshToken);
                        if(admin != null) {
                            String newAccessToken = this.tokenProvider.generateToken(admin);
                            this.cookieService.addAccessTokenToCookie(newAccessToken, response);
                            Authentication newAuthentication = jwtUtil.getAuthentication(newAccessToken);
                            SecurityContextHolder.getContext().setAuthentication(newAuthentication);
                            chain.doFilter(request, response);
                            return;
                        }
                    }
                    handleException(TOKEN_REISSUE_FAILED, request, response);
            case MALFORMED:
                   handleException(AUTHORIZATION_FAILED, request, response);
            case INVALID:
                   handleException(INVALID_TOKEN, request, response);
            }
        } catch (Exception e) {
            handleException(AUTHORIZATION_FAILED, request, response);
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        return !((path.startsWith("/admin/") && method.equals("DELETE")) ||
                (path.startsWith("/categories") && Set.of("POST", "PUT", "DELETE").contains(method)));
    }

    private void handleException(AuthErrorCode ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.cookieService.clearTokenFromCookie(response);
        response.setStatus(ex.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse errorResponse = ErrorResponse.of(
                ex.getMessage(),
                ex.getCode(),
                request.getRequestURI()
        );
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}