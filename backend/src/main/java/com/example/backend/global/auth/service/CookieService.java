package com.example.backend.global.auth.service;

import org.springframework.stereotype.Service;

import com.example.backend.global.auth.util.CookieUtil;
import com.example.backend.global.auth.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * CookieService
 * 쿠키 관련 로직을 처리하는 클래스
 * @author 100mi
 */
@Service
@RequiredArgsConstructor
public class CookieService {

	private final CookieUtil cookieUtil;
	private final JwtUtil jwtUtil;

	public void addAccessTokenToCookie(String accessToken, HttpServletResponse response) {
		cookieUtil.addTokenToCookie("accessToken", accessToken,
			jwtUtil.getAccessTokenExpirationTime(), response);
	}

	public void addRefreshTokenToCookie(String refreshToken, HttpServletResponse response) {
		cookieUtil.addTokenToCookie("refreshToken", refreshToken,
			jwtUtil.getRefreshTokenExpirationTime(), response);
	}

	public String getAccessTokenFromCookie(HttpServletRequest request) {
		return cookieUtil.getTokenFromCookie("accessToken", request);
	}

	public String getRefreshTokenFromCookie(HttpServletRequest request) {
		return cookieUtil.getTokenFromCookie("refreshToken", request);
	}

	public void clearTokenFromCookie(HttpServletResponse response) {
		cookieUtil.addTokenToCookie("accessToken", null, 0, response);
		cookieUtil.addTokenToCookie("refreshToken", null, 0, response);
	}
}
