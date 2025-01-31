package com.example.backend.domain.voter.service;

import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.repository.MemberRepository;
import com.example.backend.domain.voter.dto.VoterDTO;
import com.example.backend.domain.voter.entity.Voter;
import com.example.backend.domain.voter.repository.VoterRepository;
import com.example.backend.domain.vote.entity.Vote;
import com.example.backend.domain.vote.repository.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class VoterService {
    private final VoterRepository voterRepository;
    private final VoteRepository voteRepository;
    private final MemberRepository memberRepository;

    // 특정 투표에 참여한 Voter 목록 조회
    public List<VoterDTO> getVotersByVoteId(Long voteId) {
        List<Voter> voters = voterRepository.findByIdVoteId(voteId);
        return voters.stream()
                .map(VoterDTO::from) // Voter 엔티티를 VoterDTO로 변환
                .collect(Collectors.toList());
    }

    // 투표 참여 기능
    @Transactional
    public Voter addVoter(Long voteId, Long memberId) {
        // 투표 및 사용자 조회
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 중복 참여 방지
        Voter.VoterId voterId = new Voter.VoterId(memberId, voteId);
        if (voterRepository.existsById(voterId)) {
            throw new IllegalArgumentException("이미 참여한 투표입니다.");
        }

        // Voter 엔티티 생성 및 저장
        Voter voter = new Voter(voterId, member, vote);
        return voterRepository.save(voter);
    }
}

