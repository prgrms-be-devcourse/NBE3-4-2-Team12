package com.example.backend.domain.group.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JoinGroupRequestDto {
    private Long groupId;
    private Long memberId;
}
