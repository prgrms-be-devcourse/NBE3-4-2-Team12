package com.example.backend.domain.category;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.example.backend.domain.category.dto.CategoryRequestDto;
import com.example.backend.domain.category.dto.CategoryResponseDto;
import com.example.backend.domain.category.entity.Category;
import com.example.backend.domain.category.entity.CategoryType;
import com.example.backend.domain.category.repository.CategoryRepository;
import com.example.backend.domain.category.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryRequestDto categoryRequestDto;

    @BeforeEach
    void setUp() {
        categoryRequestDto = new CategoryRequestDto(
                CategoryType.STUDY,
                "category1"
        );
    }

    @Test
    @DisplayName("카테고리 생성 테스트")
    void createCategoryTest() {
        // Given
        Category category = Category.builder()
                .name(categoryRequestDto.getName())
                .categoryType(categoryRequestDto.getType())
                .build();

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // When
        CategoryResponseDto response = categoryService.create(categoryRequestDto);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(category.getName());
        assertThat(response.getType()).isEqualTo(category.getCategoryType());

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("카테고리 수정 테스트")
    void modifyCategoryTest() {
        Category oldCategory = new Category("oldCategory",CategoryType.EXERCISE);
        when(categoryRepository.findById(oldCategory.getId())).thenReturn(Optional.of(oldCategory));
        CategoryResponseDto response = categoryService.modify(oldCategory.getId(), categoryRequestDto);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(categoryRequestDto.getName());
        assertThat(response.getType()).isEqualTo(categoryRequestDto.getType());

        verify(categoryRepository, times(1)).findById(oldCategory.getId());
    }


}