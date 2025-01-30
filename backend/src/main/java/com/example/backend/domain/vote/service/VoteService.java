package com.example.backend.domain.vote.service;

import com.example.backend.domain.vote.dto.VoteDTO;
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
    public VoteDTO createVote(Long groupId, VoteDTO voteDTO) {
        Vote vote = voteDTO.toEntity(groupId);
        Vote savedVote = voteRepository.save(vote);
        return VoteDTO.toDTO(savedVote);
    }
}
