package com.example.backend.domain.vote.service;

import com.example.backend.domain.vote.dto.VoteRequestDto;
import com.example.backend.domain.vote.dto.VoteResponseDto;
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
    public VoteResponseDto createVote(Long groupId, VoteRequestDto request) {
        Vote vote = request.toEntity(groupId);
        Vote savedVote = voteRepository.save(vote);

        return VoteResponseDto.toDto(savedVote);
    }
}
