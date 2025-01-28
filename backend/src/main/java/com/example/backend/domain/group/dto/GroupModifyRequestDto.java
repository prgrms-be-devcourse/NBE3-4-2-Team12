package com.example.backend.domain.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupModifyRequestDto {
    private Long groupId;
    private String title;
    private String description;
    private Integer maxParticipants;
}
