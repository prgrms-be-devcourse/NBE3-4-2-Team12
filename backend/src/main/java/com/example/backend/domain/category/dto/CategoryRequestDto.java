package com.example.backend.domain.category.dto;

import com.example.backend.domain.category.entity.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryRequestDto {

    @NotNull(message = "타입은 필수 항목입니다.")
    private CategoryType type;

    @NotBlank(message = "이름은 필수 항목입니다.")
    private String name;
}
