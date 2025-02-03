package com.example.backend.domain.category.service;

import com.example.backend.domain.category.dto.CategoryRequestDto;
import com.example.backend.domain.category.dto.CategoryResponseDto;
import com.example.backend.domain.category.entity.Category;
import com.example.backend.domain.category.exception.CategoryErrorCode;
import com.example.backend.domain.category.exception.CategoryException;
import com.example.backend.domain.category.repository.CategoryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public CategoryResponseDto modify(Long id,@Valid CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new CategoryException(CategoryErrorCode.NOT_FOUND));
        category.modify(
                categoryRequestDto.getName(),
                categoryRequestDto.getType()
        );
        categoryRepository.save(category);
        return new CategoryResponseDto(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new CategoryException(CategoryErrorCode.NOT_FOUND_LIST);
        }
        return categories.stream().map(CategoryResponseDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto getCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new CategoryException(CategoryErrorCode.NOT_FOUND));
        return new CategoryResponseDto(category);
    }

    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new CategoryException(CategoryErrorCode.NOT_FOUND));
        categoryRepository.delete(category);
    }
}
