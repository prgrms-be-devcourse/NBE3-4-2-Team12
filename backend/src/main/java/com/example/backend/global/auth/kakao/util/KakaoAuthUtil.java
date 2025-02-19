package com.example.backend.global.auth.kakao.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * KakaoAuthUtil
 * 카카오 인증에서 사용하는 Util클래스 ApiKey, RedirectUri등 제어
 * @author 100minha
 */
@Component
public class KakaoAuthUtil {

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String CLIENT_ID;

	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String REDIRECT_URI;

	@Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
	private String GRANT_TYPE;

	@Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
	private String AUTHORIZATION_URI;

	@Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
	private String TOKEN_URI;

	@Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
	private String USER_INFO_URI;

	@Value("${KAKAO_LOGOUT_URL}")
	private String KAKAO_LOGOUT_URL;

	@Value("${KAKAO_LOGOUT_REDIRECT_URI}")
	private String KAKAO_LOGOUT_REDIRECT_URI;

	public String getKakaoAuthorizationUrl() {

		return UriComponentsBuilder.fromUriString(AUTHORIZATION_URI)
			.queryParam("response_type", "code")
			.queryParam("client_id", CLIENT_ID)
			.queryParam("redirect_uri", REDIRECT_URI)
			.toUriString();
	}

	public String getKakaoLoginTokenUrl(String authorizationCode) {

		return UriComponentsBuilder.fromUriString(TOKEN_URI)
			.queryParam("grant_type", GRANT_TYPE)
			.queryParam("client_id", CLIENT_ID)
			.queryParam("redirect_uri", REDIRECT_URI)
			.queryParam("code", authorizationCode)
			.toUriString();
	}

	public String getUserInfoUrl() {
		return USER_INFO_URI;
	}

	public String getLogoutUrl(Long userId) {

		return UriComponentsBuilder.fromUriString(KAKAO_LOGOUT_URL)
			.queryParam("client_id", CLIENT_ID)
			.queryParam("logout_redirect_uri", KAKAO_LOGOUT_REDIRECT_URI)
			.queryParam("state", userId)
			.toUriString();
	}

	public String getKakaoTokenReissueUrl(String refreshToken) {

		return UriComponentsBuilder.fromUriString(TOKEN_URI)
			.queryParam("grant_type", "refresh_token")
			.queryParam("client_id", CLIENT_ID)
			.queryParam("refresh_token", refreshToken)
			.toUriString();
	}
}