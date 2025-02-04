package com.example.backend.domain.voter.service;

import com.example.backend.domain.voter.dto.VoterDTO;
import com.example.backend.domain.voter.entity.Voter;
import com.example.backend.domain.voter.repository.VoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class VoterService {
	private final VoterRepository voterRepository;
	// 특정 투표에 참여한 Voter 목록 조회
	public List<VoterDTO> getVotersByVoteId(Long voteId) {
		List<Voter> voters = voterRepository.findByIdVoteId(voteId);
		return voters.stream()
			.map(VoterDTO::from) // Voter 엔티티를 VoterDTO로 변환
			.collect(Collectors.toList());
	}
}