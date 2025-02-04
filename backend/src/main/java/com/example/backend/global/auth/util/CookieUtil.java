package com.example.backend.global.auth.util;

import java.util.Arrays;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * CookieUtil
 * 쿠키에 관련된 로직을 수행할 유틸 클래스
 * @author 100minha
 */
@Component
public class CookieUtil {

	/**
	 * 쿠키에 name의 토큰을 저장하는 메서드
	 * @param name
	 * @param value
	 * @param expiration
	 * @param response
	 */
	public void addTokenToCookie(String name, String value, long expiration, HttpServletResponse response) {
		ResponseCookie cookie = ResponseCookie.from(name, value)
			.httpOnly(true)
			.secure(true)
			.path("/")
			.sameSite("Strict")
			.maxAge(expiration)
			.build();

		response.addHeader("Set-Cookie", cookie.toString());
	}

	/**
	 * 쿠키에서 name의 토큰을 받아오는 메서드
	 * @param name
	 * @param request
	 * @return
	 */
	public String getTokenFromCookie(String name, HttpServletRequest request) {
		if (request.getCookies() == null)
			return null;

		return Arrays.stream(request.getCookies())
			.filter(cookie -> name.equals(cookie.getName()))
			.map(Cookie::getValue)
			.findFirst()
			.orElse(null);
	}

}
