package com.example.backend.domain.member.dto;

import com.example.backend.domain.member.entity.Member;

import lombok.Builder;

/**
 * MemberTokenReissueDto
 * 토큰 갱신할 때 사용할 리프레시토큰까지 갖는 Dto
 * @author 100minha
 */
@Builder
public record MemberTokenReissueDto(
	Long id,
	String nickname,
	String email,
	String refreshToken
) {

	public static MemberTokenReissueDto of(Member member) {
		return MemberTokenReissueDto.builder()
			.id(member.getId())
			.nickname(member.getNickname())
			.email(member.getEmail())
			.refreshToken(member.getKakaoRefreshToken())
			.build();
	}
}
