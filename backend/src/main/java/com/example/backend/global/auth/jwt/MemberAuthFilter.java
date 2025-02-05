package com.example.backend.global.auth.jwt;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.backend.global.auth.model.CustomUserDetails;
import com.example.backend.global.auth.service.CookieService;
import com.example.backend.global.auth.service.CustomUserDetailService;
import com.example.backend.global.auth.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * MemberAuthFilter
 * 사용자 자체 Jwt토큰 필터
 * @author 100minha
 */
@Configuration
@RequiredArgsConstructor
public class MemberAuthFilter extends OncePerRequestFilter {

	private final CookieService cookieService;
	private final CustomUserDetailService customUserDetailService;
	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String accessToken = cookieService.getAccessTokenFromCookie(request);
		if (accessToken == null) {
			String refreshToken = cookieService.getRefreshTokenFromCookie(request);
			if (refreshToken == null) {
				return;
			}
			return;
		}

		// TODO: 토큰 검증 로직 추가
		CustomUserDetails customUserDetails = customUserDetailService.loadUserByUsername(
			jwtUtil.getId(accessToken));
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
			customUserDetails, null, customUserDetails.getAuthorities()
		);

		SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		filterChain.doFilter(request, response);
	}
}
