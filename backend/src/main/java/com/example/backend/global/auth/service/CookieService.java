package com.example.backend.global.auth.service;

import org.springframework.stereotype.Service;

import com.example.backend.global.auth.util.CookieUtil;

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
	
}
