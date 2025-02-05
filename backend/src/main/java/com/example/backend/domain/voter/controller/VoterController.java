package com.example.backend.domain.voter.controller;

import com.example.backend.domain.voter.dto.VoterDTO;
import com.example.backend.domain.voter.service.VoterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/voters")
public class VoterController {

    private final VoterService voterService;

    // 특정 투표 참여 API 추가
    @PostMapping("/{groupId}/{voteId}/{memberId}")
    public ResponseEntity<VoterDTO> addVoter(@PathVariable Long groupId,
                                             @PathVariable Long voteId,
                                             @PathVariable Long memberId) {
        log.info("New voter participation requested: groupId={}, voteId={}, memberId={}", groupId, voteId, memberId);
        VoterDTO response = VoterDTO.from(voterService.addVoter(groupId, voteId, memberId));
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    // 특정 투표에 참여한 Voter 목록 조회 API
    @GetMapping("/{voteId}")
    public ResponseEntity<List<VoterDTO>> getVotersByVote(@PathVariable Long voteId) {
        log.info("Getting voter list for voteId={}", voteId);
        List<VoterDTO> response = voterService.getVotersByVoteId(voteId);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }
}
