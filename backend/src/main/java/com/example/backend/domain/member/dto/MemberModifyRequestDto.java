package com.example.backend.domain.member.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * MemberModifyRequestDto
 * 사용자 정보 수정에 사용할 Dto
 * @author 100minha
 */
@Builder
public record MemberModifyRequestDto(
	@NotBlank(message = "닉네임은 공백일 수 없습니다.")
	@Length(min = 2, max = 10, message = "닉네임은 최소 2글자 이상, 10글자 이하이어야 합니다.")
	String nickname
) {
}
