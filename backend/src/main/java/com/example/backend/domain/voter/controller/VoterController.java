package com.example.backend.domain.voter.controller;

import com.example.backend.domain.voter.dto.VoterDTO;
import com.example.backend.domain.voter.service.VoterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/voters")
public class VoterController {

    private final VoterService voterService;

    // 특정 투표에 참여한 Voter 목록 조회 API
    @GetMapping("/{voteId}")
    public List<VoterDTO> getVotersByVote(@PathVariable Long voteId) {
        return voterService.getVotersByVoteId(voteId);
    }

}
