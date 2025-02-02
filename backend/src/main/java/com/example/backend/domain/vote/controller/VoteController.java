package com.example.backend.domain.vote.controller;



import com.example.backend.domain.vote.dto.VoteRequestDto;
import com.example.backend.domain.vote.dto.VoteResponseDto;
import com.example.backend.domain.vote.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/votes")
public class VoteController {
    private final VoteService voteService;

    // 생성
    @PostMapping("/groups/{groupId}/votes")
    public ResponseEntity<VoteResponseDto> createVote(
            @PathVariable Long groupId,
            @RequestBody VoteRequestDto request)
    {
        return ResponseEntity.ok(voteService.createVote(groupId, request));
    }

    //투표 목록조회
    @GetMapping("/groups/{groupId}/votes")
    public ResponseEntity<List<VoteResponseDto>> getVotesByGroupId(
            @PathVariable Long groupId)
    {
        return ResponseEntity.ok(voteService.findAllByGroupId(groupId));
    }

    //단일 투표 상세조회
    @GetMapping("/groups/{groupId}/votes/{voteId}")
    public ResponseEntity<VoteResponseDto> getVote(
            @PathVariable Long groupId,
            @PathVariable Long voteId)
    {
        return ResponseEntity.ok(voteService.findById(groupId, voteId));
    }

    //투표 수정
    @PutMapping("/groups/{groupId}/votes/{voteId}")
    public ResponseEntity<VoteResponseDto> modifyVote(
            @PathVariable Long groupId,
            @PathVariable Long voteId,
            @Valid @RequestBody VoteRequestDto requestDto)
    {
        return ResponseEntity.ok(voteService.modifyVote(groupId, voteId, requestDto));
    }

    @DeleteMapping("/groups/{groupId}/votes/{voteId}")
    public ResponseEntity<Void> deleteVote(
            @PathVariable Long groupId,
            @PathVariable Long voteId)
    {
        voteService.deleteVote(groupId, voteId);
        return ResponseEntity.ok().build();
    }
}
