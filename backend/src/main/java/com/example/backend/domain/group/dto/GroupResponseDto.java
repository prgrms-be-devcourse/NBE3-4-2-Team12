package com.example.backend.domain.group.dto;

import com.example.backend.domain.group.entity.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponseDto {
    private Long id;
    private String title;
    private String description;
    private Integer maxParticipants;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public GroupResponseDto(Group group) {
        this.id = group.getId();
        this.title = group.getTitle();
        this.description = group.getDescription();
        this.maxParticipants = group.getMaxParticipants();
        this.createdAt = group.getCreatedAt();
        this.modifiedAt = group.getModifiedAt();
    }
}
