package com.example.backend.domain.member.dto;

import lombok.Builder;

/**
 * MemberResponseDto
 * <p></p>
 * @author 100minha
 */
@Builder
public record MemberResponseDto(
	Long id,
	String nickname,
	String email
) {

}
