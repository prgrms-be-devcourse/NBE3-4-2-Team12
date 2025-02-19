package com.example.backend.global.auth.kakao.dto;

import lombok.Builder;

/**
 * LoginResponse
 * <p></p>
 * @author 100minha
 */
@Builder
public record LoginResponseDto(
	String nickname,
	String accessToken,
	String refreshToken
) {
}
