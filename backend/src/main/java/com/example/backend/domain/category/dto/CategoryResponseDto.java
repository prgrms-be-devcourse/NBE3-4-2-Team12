package com.example.backend.domain.category.dto;

import com.example.backend.domain.category.entity.Category;
import com.example.backend.domain.category.entity.CategoryType;
import lombok.Getter;

@Getter
public class CategoryResponseDto {
    private Long id;
    private CategoryType type;
    private String name;


    public CategoryResponseDto(Category category) {
        this.id = category.getId();
        this.type = category.getCategoryType();
        this.name = category.getName();
    }
}
