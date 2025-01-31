package com.example.backend.domain.vote.dto;

import com.example.backend.domain.vote.entity.Vote;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class VoteResponseDto {
    private Long id;
    private String location;
    private String address;
    private Double latitude;
    private Double longitude;
    private Integer voterCount;
    private LocalDateTime createdAt;

    public static VoteResponseDto toDto(Vote vote) {
        return VoteResponseDto.builder()
                .id(vote.getId())
                .location(vote.getLocation())
                .address(vote.getAddress())
                .latitude(vote.getLatitude())
                .longitude(vote.getLongitude())
                .voterCount(vote.getVoterCount())
                .createdAt(vote.getCreatedAt())
                .build();
    }
}