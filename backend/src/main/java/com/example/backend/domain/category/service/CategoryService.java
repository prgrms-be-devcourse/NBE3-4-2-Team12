package com.example.backend.domain.category.service;

import com.example.backend.domain.category.dto.CategoryRequestDto;
import com.example.backend.domain.category.dto.CategoryResponseDto;
import com.example.backend.domain.category.entity.Category;
import com.example.backend.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponseDto create(CategoryRequestDto requestDto){
        Category category = Category.builder()
                .name(requestDto.getName())
                .categoryType(requestDto.getType())
                .build();
        categoryRepository.save(category);
        return new CategoryResponseDto(category);
    }
}
