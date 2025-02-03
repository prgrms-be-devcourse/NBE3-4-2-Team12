package com.example.backend.domain.category.service;

import com.example.backend.domain.category.dto.CategoryRequestDto;
import com.example.backend.domain.category.dto.CategoryResponseDto;
import com.example.backend.domain.category.entity.Category;
import com.example.backend.domain.category.repository.CategoryRepository;
import com.example.backend.domain.group.exception.GroupErrorCode;
import com.example.backend.domain.group.exception.GroupException;
import jakarta.validation.Valid;
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

    public CategoryResponseDto modify(Long id,@Valid CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new GroupException(GroupErrorCode.NOT_FOUND));
        category.modify(
                categoryRequestDto.getName(),
                categoryRequestDto.getType()
        );
        categoryRepository.save(category);
        return new CategoryResponseDto(category);
    }
}
