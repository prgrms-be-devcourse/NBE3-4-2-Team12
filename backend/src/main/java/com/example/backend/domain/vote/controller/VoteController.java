package com.example.backend.domain.vote.controller;

import com.example.backend.domain.vote.dto.VoteDTO;
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
    public ResponseEntity<VoteDTO> createVote(@PathVariable Long groupId,
                                              @RequestBody VoteDTO voteDTO) {
        return ResponseEntity.ok(voteService.createVote(groupId, voteDTO));
    }
}
