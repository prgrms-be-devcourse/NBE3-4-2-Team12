package com.example.backend.domain.voter.dto;

import com.example.backend.domain.voter.entity.Voter;
import com.example.backend.domain.vote.entity.Vote;
import com.example.backend.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoterDTO {
    private Long memberId;  // 투표자 ID
    private Long voteId;  // 투표 ID

    // Entity -> DTO
    public static VoterDTO from(Voter voter) {
        VoterDTO voterDto = new VoterDTO();
        voterDto.memberId = voter.getId().getMemberId();
        voterDto.voteId = voter.getId().getVoteId();
        return voterDto;
    }

    public Voter toEntity(Member member, Vote vote) {
        return Voter.builder()
                .id(new Voter.VoterId(memberId, voteId))
                .member(member)
                .vote(vote)
                .build();
    }
}
