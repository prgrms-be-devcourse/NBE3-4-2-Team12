package com.example.backend.global.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KakaoResponseBodyDto
 * <p></p>
 * @author 100minha
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoTokenResponseDto(
	@JsonProperty("token_type")
	String tokenType,
	@JsonProperty("access_token")
	String accessToken,
	@JsonProperty("expires_in")
	String expiresIn,
	@JsonProperty("refresh_token")
	String refreshToken,
	@JsonProperty("refresh_token_expires_in")
	String refreshTokenExpiresIn
) {
}
