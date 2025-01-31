package com.example.backend.domain.voter.dto;

import com.example.backend.domain.voter.entity.Voter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoterDTO {
    private Long userId;  // 투표자 ID
    private Long voteId;  // 투표 ID
    private Long groupId; // 모임 ID

    // Entity -> DTO
    public static VoterDTO from(Voter voter) {
        VoterDTO voterDto = new VoterDTO();
        voterDto.userId = voter.getId().getUserId();
        voterDto.voteId = voter.getId().getVoteId();
//        voterDto.groupId = voter.getId().getGroupId();
        return voterDto;
    }
}
