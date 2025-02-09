package com.example.backend.domain.member.dto;

import com.example.backend.domain.member.entity.Member;

import lombok.Builder;

/**
 * MemberResponseDto
 * <p></p>
 * @author 100minha
 */
@Builder
public record MemberInfoDto(
	Long id,
	String nickname,
	String email
) {

	public static MemberInfoDto of(Member member) {
		return MemberInfoDto.builder()
			.id(member.getId())
			.nickname(member.getNickname())
			.email(member.getEmail())
			.build();
	}
}
