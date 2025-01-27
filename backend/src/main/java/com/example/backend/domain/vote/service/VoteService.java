package com.example.backend.domain.vote.service;

import com.example.backend.domain.vote.entity.Vote;
import com.example.backend.domain.vote.repository.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VoteService {
    private final VoteRepository voteRepository;

    @Transactional
    public Vote createVote(Long groupId, Vote vote){
        vote.setGroupId(groupId);
        vote.setVoterCount(0);
        return voteRepository.save(vote);
    }
}
