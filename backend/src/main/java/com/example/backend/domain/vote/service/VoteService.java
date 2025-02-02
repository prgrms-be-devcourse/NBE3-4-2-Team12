package com.example.backend.domain.vote.service;

import com.example.backend.domain.group.service.GroupService;
import com.example.backend.domain.vote.dto.VoteRequestDto;
import com.example.backend.domain.vote.dto.VoteResponseDto;
import com.example.backend.domain.vote.entity.Vote;
import com.example.backend.domain.vote.repository.VoteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class VoteService {
    private final VoteRepository voteRepository;

    private final GroupService groupService;

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
}
