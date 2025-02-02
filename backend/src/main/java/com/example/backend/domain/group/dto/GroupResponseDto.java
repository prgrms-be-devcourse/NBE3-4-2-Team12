package com.example.backend.domain.group.dto;

import com.example.backend.domain.group.entity.Group;
import com.example.backend.domain.group.entity.GroupStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GroupResponseDto {
    private Long id;
    private String title;
    private String description;
    private Long memberId;
    private Integer maxParticipants;
    private GroupStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public GroupResponseDto(Group group) {
        this.id = group.getId();
        this.title = group.getTitle();
        this.description = group.getDescription();
        this.memberId = group.getMember().getId();
        this.maxParticipants = group.getMaxParticipants();
        this.status = group.getStatus();
        this.createdAt = group.getCreatedAt();
        this.modifiedAt = group.getModifiedAt();
    }
}
