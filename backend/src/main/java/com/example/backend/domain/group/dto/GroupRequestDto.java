package com.example.backend.domain.group.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequestDto {
    private String title;
    private String description;
    private Integer maxParticipants;
}
