package com.example.backend.global.auth.jwt;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.backend.domain.member.dto.MemberTokenReissueDto;
import com.example.backend.domain.member.exception.MemberException;
import com.example.backend.global.auth.exception.AuthErrorCode;
import com.example.backend.global.auth.exception.AuthException;
import com.example.backend.global.auth.model.CustomUserDetails;
import com.example.backend.global.auth.service.CookieService;
import com.example.backend.global.auth.service.CustomUserDetailService;
import com.example.backend.global.auth.service.KakaoAuthService;
import com.example.backend.global.auth.util.JwtUtil;
import com.example.backend.global.auth.util.TokenProvider;
import com.example.backend.global.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MemberAuthFilter
 * 사용자 자체 Jwt토큰 필터
 * @author 100minha
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class MemberAuthFilter extends OncePerRequestFilter {

	private final CookieService cookieService;
	private final CustomUserDetailService customUserDetailService;
	private final KakaoAuthService kakaoAuthService;
	private final JwtUtil jwtUtil;
	private final TokenProvider tokenProvider;

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		final String accessToken = cookieService.getAccessTokenFromCookie(request);
		final String refreshToken = cookieService.getRefreshTokenFromCookie(request);

		// 엑세스 토큰과 리프레쉬 토큰이 null일 경우 에러 응답
		// 엑세스 토큰만 null일 경우 리프레쉬 토큰으로 토큰 재발급 후 인증
		if (accessToken == null) {
			if (refreshToken == null) {
				handleAuthError(new AuthException(AuthErrorCode.AUTHORIZATION_FAILED), request, response);
				return;
			}
			String reissuedAccessToken = reissueToken(refreshToken, request, response);
			setAuthenticationInContext(reissuedAccessToken);
			filterChain.doFilter(request, response);
			return;
		}

		try {
			TokenStatus tokenStatus = jwtUtil.validateToken(accessToken);

			switch (tokenStatus) {
				case VALID:
					setAuthenticationInContext(accessToken);
					break;

				case EXPIRED:
					log.info("만료된 토큰입니다.");
					String reissuedAccessToken = reissueToken(refreshToken, request, response);
					setAuthenticationInContext(reissuedAccessToken);
					break;

				case MALFORMED, INVALID:
					log.error("잘못된 형식의 토큰입니다.");
					handleAuthError(new AuthException(AuthErrorCode.INVALID_TOKEN), request, response);
					return;
			}
		} catch (Exception e) {
			log.error("필터 내부에서 예상치 못한 예외 발생: {}", e.getMessage());
			handleAuthError(new AuthException(AuthErrorCode.AUTHORIZATION_FAILED), request, response);
			return;
		}
		filterChain.doFilter(request, response);
	}

	/**
	 * 사용자 권한이 필요한 api 경로만 필터링을 하는 메서드
	 * @param request
	 * @return
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {

		String path = request.getRequestURI();
		String method = request.getMethod();

		return !(
			(method.equals("POST") && path.startsWith("/groups")) ||
				(method.equals("PUT") && path.startsWith("/groups")) ||
				(method.equals("DELETE") && path.startsWith("/groups")) ||
				path.startsWith("/members") ||
				path.startsWith("/votes") ||
				path.startsWith("/voters") ||
				path.equals("/auth/kakao/logout")
		);
	}

	/**
	 * SecurityContext에 인증 정보를 주입하는 메서드
	 * @param accessToken
	 */
	private void setAuthenticationInContext(String accessToken) {

		CustomUserDetails customUserDetails = customUserDetailService.loadUserByUsername(
			jwtUtil.getId(accessToken));
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
			customUserDetails, null, customUserDetails.getAuthorities()
		);

		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}

	/**
	 * 인증에 실패했을 때 로그아웃 처리 및 에러 응답 메서드
	 * @param ex
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void handleAuthError(AuthException ex,
		HttpServletRequest request, HttpServletResponse response) throws IOException {

		cookieService.clearTokenFromCookie(response);
		SecurityContextHolder.clearContext();

		response.setStatus(ex.getStatus().value());
		response.setContentType("application/json;charset=UTF-8");

		ErrorResponse errorResponse = ErrorResponse.of(ex.getMessage(), ex.getCode(), request.getRequestURI());
		objectMapper.writeValue(response.getWriter(), errorResponse);
	}

	/**
	 * 토큰 재발급 및 쿠키에 토큰 정보 저장하는 메서드
	 * @param refreshToken
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private String reissueToken(String refreshToken, HttpServletRequest request, HttpServletResponse response) throws
		IOException {
		try {
			MemberTokenReissueDto memberTokenReissueDto = kakaoAuthService.reissueTokens(refreshToken);

			String reissuedAccessToken = tokenProvider.generateMemberAccessToken(
				memberTokenReissueDto.id(), memberTokenReissueDto.nickname(),
				memberTokenReissueDto.email());

			cookieService.addAccessTokenToCookie(reissuedAccessToken, response);
			cookieService.addRefreshTokenToCookie(memberTokenReissueDto.refreshToken(), response);

			return reissuedAccessToken;
		} catch (MemberException e) {
			log.error("유효하지 않은 인증정보입니다.");
			handleAuthError(new AuthException(AuthErrorCode.INVALID_TOKEN), request, response);
		} catch (Exception e) {
			log.error("토큰 갱신 중 오류 발생: {}", e.getMessage());
			handleAuthError(new AuthException(AuthErrorCode.TOKEN_REISSUE_FAILED), request, response);
		}
		return null;
	}
}
