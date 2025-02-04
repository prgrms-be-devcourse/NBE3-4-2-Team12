package com.example.backend.global.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KakaoUserInfoResponseDto
 * 카카오에서 응답해준 유저 정보를 담는 dto
 * @author 100minha
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoUserInfoResponseDto(@JsonProperty("id") Long id,
									   @JsonProperty("properties") Properties properties,
									   @JsonProperty("kakao_account") KakaoAccount kakaoAccount) {
	public record Properties(
		@JsonProperty("nickname") String nickname,
		@JsonProperty("profile_image") String profileImage
	) {
	}

	public record KakaoAccount(
		@JsonProperty("email") String email
	) {
	}
}
