package com.example.backend.domain.vote.controller;

import com.example.backend.domain.vote.dto.VoteRequestDto;
import com.example.backend.domain.vote.dto.VoteResponseDto;
import com.example.backend.domain.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/votes")
public class VoteController {
    private final VoteService voteService;

    // Controller
    @PostMapping("/groups/{groupId}/votes")
    public ResponseEntity<VoteResponseDto> createVote(
            @PathVariable Long groupId,
            @RequestBody VoteRequestDto request  // <- 여기서 VoteRequest DTO로 변환
    ) {
        return ResponseEntity.ok(voteService.createVote(groupId, request));
    }
}
