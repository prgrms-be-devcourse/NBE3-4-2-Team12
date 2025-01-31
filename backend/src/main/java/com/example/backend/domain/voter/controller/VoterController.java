package com.example.backend.domain.voter.controller;

import com.example.backend.domain.voter.dto.VoterDTO;
import com.example.backend.domain.voter.entity.Voter;
import com.example.backend.domain.voter.service.VoterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voters")
public class VoterController {

    private final VoterService voterService;

    // 특정 투표 참여 API 추가
    @PostMapping("/{voteId}/{memberId}")
    public ResponseEntity<VoterDTO> addVoter(@PathVariable Long voteId, @PathVariable Long memberId) {
        return ResponseEntity.ok(VoterDTO.from(voterService.addVoter(voteId, memberId)));
    }

    // 특정 투표에 참여한 Voter 목록 조회 API
    @GetMapping("/{voteId}")
    public List<VoterDTO> getVotersByVote(@PathVariable Long voteId) {
        return voterService.getVotersByVoteId(voteId);
    }

}
