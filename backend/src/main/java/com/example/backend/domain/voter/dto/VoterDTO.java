package com.example.backend.domain.voter.dto;

import com.example.backend.domain.voter.entity.Voter;
import com.example.backend.domain.vote.entity.Vote;
import com.example.backend.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
public class VoterDTO {
    private Long memberId;      // 투표자 ID
    private Long voteId;        // 투표 ID
    private String nickname;    // 투표자 닉네임 추가
    private LocalDateTime votedAt;  // 투표 시간 추가

    // Entity -> DTO
    public static VoterDTO from(Voter voter) {
        return VoterDTO.builder()
                .memberId(voter.getId().getMemberId())
                .voteId(voter.getId().getVoteId())
                .nickname(voter.getMember().getNickname())
                .votedAt(voter.getCreatedAt())
                .build();
    }
    public Voter toEntity(Member member, Vote vote) {
        return Voter.builder()
                .id(new Voter.VoterId(memberId, voteId))
                .member(member)
                .vote(vote)
                .build();
    }
}
