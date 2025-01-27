package com.example.backend.domain.vote.controller;

import com.example.backend.domain.vote.dto.VoteDTO;
import com.example.backend.domain.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/votes")
public class VoteController {
    private final VoteService voteService;

    @PostMapping("/{groupId}")
    public VoteDTO createVote(@PathVariable Long groupId,
                              @RequestBody VoteDTO voteDTO){
        return VoteDTO.from(voteService.createVote(groupId, voteDTO.toEntity(groupId)));
    }
}
