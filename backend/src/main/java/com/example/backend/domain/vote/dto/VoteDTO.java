package com.example.backend.domain.vote.dto;

import com.example.backend.domain.vote.entity.Vote;
import lombok.Getter;
import org.hibernate.bytecode.internal.bytebuddy.BytecodeProviderImpl;

@Getter
public class VoteDTO {
    private String location;
    private String address;
    private Double latitude;
    private Double longitude;

    // Entity -> DTO
    public static VoteDTO toDTO(Vote vote) {
        VoteDTO voteDto = new VoteDTO();
        voteDto.location = vote.getLocation();
        voteDto.address = vote.getAddress();
        voteDto.latitude = vote.getLatitude();
        voteDto.longitude = vote.getLongitude();
        return voteDto;
    }

    // DTO -> Entity
    public Vote toEntity(Long groupId) {
        return Vote.builder()
                .groupId(groupId)
                .location(this.location)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .voterCount(0)
                .build();
    }
}
