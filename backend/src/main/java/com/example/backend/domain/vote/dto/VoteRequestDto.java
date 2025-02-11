package com.example.backend.domain.vote.dto;

import com.example.backend.domain.vote.entity.Vote;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VoteRequestDto {
    private String location;
    private String address;
    private Double latitude;
    private Double longitude;

    public Vote toEntity(Long groupId){
        return Vote.builder()
                .groupId(groupId)
                .location(this.location)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
//                .voterCount(0)
                .build();
    }
}
