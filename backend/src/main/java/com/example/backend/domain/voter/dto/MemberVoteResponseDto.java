package com.example.backend.domain.voter.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * MemberVoteReponseDto
 * <p></p>
 * @author 100mi
 */
@Getter
@AllArgsConstructor
public class MemberVoteResponseDto {
	private final List<Long> voteIds;
}
