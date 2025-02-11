package com.example.backend.domain.vote.service;

import com.example.backend.domain.group.service.GroupService;
import com.example.backend.domain.vote.dto.VoteRequestDto;
import com.example.backend.domain.vote.dto.VoteResponseDto;
import com.example.backend.domain.vote.dto.VoteResultResponseDto;
import com.example.backend.domain.vote.entity.Vote;
import com.example.backend.domain.vote.repository.VoteRepository;
import com.example.backend.domain.voter.entity.Voter;
import com.example.backend.domain.voter.repository.VoterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final VoterRepository voterRepository;

    @Transactional
    public VoteResponseDto createVote(Long groupId, VoteRequestDto request) {
        Vote vote = request.toEntity(groupId);
        Vote savedVote = voteRepository.save(vote);

        return VoteResponseDto.toDto(savedVote);
    }

    public List<VoteResponseDto> findAllByGroupId(Long groupId) {
        return voteRepository.findAllByGroupId(groupId)
                .stream()
                .map(VoteResponseDto::toDto)
                .collect(Collectors.toList());
    }

    public VoteResponseDto findById(Long groupId, Long voteId) {
        Vote vote = voteRepository.findByIdAndGroupId(voteId, groupId)
                .orElseThrow(() -> new EntityNotFoundException("Vote not found"));
        return VoteResponseDto.toDto(vote);
    }

    @Transactional
    public VoteResponseDto modifyVote(Long groupId, Long voteId, @Valid VoteRequestDto requestDto) {
        Vote vote = voteRepository.findByIdAndGroupId(voteId, groupId)
                .orElseThrow(() -> new EntityNotFoundException("Vote not found"));

        //영속성 save로 update수행
        Vote updatedVote = voteRepository.save(
                Vote.builder()
                        .id(voteId)
                        .groupId(groupId)
                        .location(requestDto.getLocation())
                        .address(requestDto.getAddress())
                        .latitude(requestDto.getLatitude())
                        .longitude(requestDto.getLongitude())
                        .voterCount(vote.getVoterCount())  // 기존 투표 수는 유지
                        .build()
        );

        return VoteResponseDto.toDto(updatedVote);
    }

    public void deleteVote(Long groupId, Long voteId) {
        Vote vote = voteRepository.findByIdAndGroupId(voteId, groupId)
                .orElseThrow(() -> new EntityNotFoundException("Vote not found"));

        voteRepository.delete(vote);
    }

    @Transactional(readOnly = true)
    public List<VoteResultResponseDto> getVoteResults(Long groupId) {
        List<Vote> votes = voteRepository.findAllByGroupId(groupId);
        return votes.stream()
                .map(vote -> {
                    List<Voter> voters = voterRepository.findByIdVoteId(vote.getId());
                    return VoteResultResponseDto.from(vote, voters);
                })
                .collect(Collectors.toList());
    }
}
