package com.example.backend.domain.group.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class GroupModifyRequestDto {
    private Long groupId;
    @NotBlank(message = "타이틀은 필수 항목입니다.")
    private String title;
    @NotBlank(message = "설명은 필수 항목입니다.")
    private String description;
    @NotNull(message = "인원은 필수 항목입니다.")
    private Integer maxParticipants;
}
