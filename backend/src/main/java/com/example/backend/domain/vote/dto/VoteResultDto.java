package com.example.backend.domain.vote.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class VoteResultDto {
    private List<MostVotedLocationDto> mostVotedLocations;
}
