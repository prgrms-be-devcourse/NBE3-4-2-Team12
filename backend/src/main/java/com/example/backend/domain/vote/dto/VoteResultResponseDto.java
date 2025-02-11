package com.example.backend.domain.vote.dto;

import com.example.backend.domain.vote.entity.Vote;
import com.example.backend.domain.voter.dto.VoterDTO;
import com.example.backend.domain.voter.entity.Voter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

// VoteResultResponseDto.java
@Getter
@Builder
public class VoteResultResponseDto {
    private Long voteId;
    private String location;
    private String address;
    private Double latitude;
    private Double longitude;
    private Integer voterCount;
    private List<VoterDTO> voters;

    public VoteResultResponseDto(Long voteId, String location, String address,
                                 Double latitude, Double longitude, Integer voterCount, List<VoterDTO> voters) {
        this.voteId = voteId;
        this.location = location;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.voterCount = voterCount;
        this.voters = voters;
    }

    public static VoteResultResponseDto from(Vote vote, List<Voter> voters) {
        return VoteResultResponseDto.builder()
                .voteId(vote.getId())
                .location(vote.getLocation())
                .address(vote.getAddress())
                .latitude(vote.getLatitude())
                .longitude(vote.getLongitude())
                .voterCount(vote.getVoterCount())
                .voters(voters.stream()
                        .map(VoterDTO::from)
                        .collect(Collectors.toList()))
                .build();
    }
}