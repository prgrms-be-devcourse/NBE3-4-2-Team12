package com.example.backend.domain.vote.service;

import com.example.backend.domain.group.service.GroupService;
import com.example.backend.domain.vote.dto.MostVotedLocationDto;
import com.example.backend.domain.vote.dto.VoteRequestDto;
import com.example.backend.domain.vote.dto.VoteResponseDto;
import com.example.backend.domain.vote.dto.VoteResultDto;
import com.example.backend.domain.vote.entity.Vote;
import com.example.backend.domain.vote.repository.VoteRepository;
import com.example.backend.domain.voter.repository.VoterRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final VoterRepository voterRepository;

    @Transactional(readOnly=true)
    public VoteResultDto getMostVotedLocations(Long groupId){
        // 1. groupId로 특정된 투표 모두 조회
       List<Vote> votes = voteRepository.findAllByGroupId(groupId);
       // 2. 각 투표별 투표자수 계산 및 정렬
        Map<Vote, Long> voteCountMap = votes.stream()
                .collect(Collectors.toMap(
                        vote -> vote,
                        vote -> voterRepository.countVoters(vote.getId())  // 새로 추가한 메서드 사용
                ));

        // 3. 최대 투표 찾기
        long maxVoteCount = voteCountMap.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0);

        // 4. 최대 투표의 장소관련 필드들 찾기
        List<MostVotedLocationDto> topLocations = voteCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() == maxVoteCount)
                .map(entry -> {
                    Vote vote = entry.getKey();
                    return MostVotedLocationDto.builder()
                            .address(vote.getAddress())
                            .latitude(vote.getLatitude())
                            .longitude(vote.getLongitude())
                            .build();
                })
                .collect(Collectors.toList());

        return VoteResultDto.builder()
                .mostVotedLocations(topLocations)
                .build();
    }


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
//                        .voterCount(vote.getVoterCount())  // 기존 투표 수는 유지
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
