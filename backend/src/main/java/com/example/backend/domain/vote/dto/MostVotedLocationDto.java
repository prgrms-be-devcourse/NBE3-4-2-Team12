package com.example.backend.domain.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MostVotedLocationDto {
    private String address;
    private Double latitude;
    private Double longitude;
}