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

		final String accessToken = cookieService.getAccessTokenFromCookie(request);
		final String refreshToken = cookieService.getRefreshTokenFromCookie(request);

		if (accessToken == null) {
			if (refreshToken == null) {
				filterChain.doFilter(request, response);
				return;
			}
			return;
		}

		TokenStatus tokenStatus = jwtUtil.validateToken(accessToken);

		switch (tokenStatus) {
			case VALID:
				//유효한 토큰일 시 SecurityContext에 사용자 인증 정보 등록
				CustomUserDetails customUserDetails = customUserDetailService.loadUserByUsername(
					jwtUtil.getId(accessToken));
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					customUserDetails, null, customUserDetails.getAuthorities()
				);

				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				break;
			case EXPIRED:
				System.out.println("토큰이 만료되었습니다.");
				break;
			case MALFORMED:
				System.out.println("잘못된 형식의 토큰입니다.");
				break;
			case INVALID:
				System.out.println("토큰이 비어있거나 올바르지 않습니다.");
				break;
		}

		filterChain.doFilter(request, response);
	}
}
