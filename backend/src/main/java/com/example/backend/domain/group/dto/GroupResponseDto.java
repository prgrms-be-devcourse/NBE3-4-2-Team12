package com.example.backend.domain.group.dto;

import com.example.backend.domain.category.dto.CategoryResponseDto;
import com.example.backend.domain.group.entity.Group;
import com.example.backend.domain.group.entity.GroupStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GroupResponseDto {
    private Long id;
    private String title;
    private String description;
    private Long memberId;
    private String author;
    private Integer maxParticipants;
    private GroupStatus status;
    private List<CategoryResponseDto> category;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public GroupResponseDto(Group group) {
        this.id = group.getId();
        this.title = group.getTitle();
        this.description = group.getDescription();
        this.author = group.getMember().getNickname();
        this.memberId = group.getMember().getId();
        this.maxParticipants = group.getMaxParticipants();
        this.status = group.getStatus();
        this.category = group.getGroupCategories().stream()
                .map(gc -> new CategoryResponseDto(gc.getCategory()))
                .collect(Collectors.toList());
        this.createdAt = group.getCreatedAt();
        this.modifiedAt = group.getModifiedAt();
    }
}
