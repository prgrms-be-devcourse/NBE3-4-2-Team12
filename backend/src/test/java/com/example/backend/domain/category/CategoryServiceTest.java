package com.example.backend.domain.category;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import com.example.backend.domain.category.dto.CategoryRequestDto;
import com.example.backend.domain.category.dto.CategoryResponseDto;
import com.example.backend.domain.category.entity.Category;
import com.example.backend.domain.category.entity.CategoryType;
import com.example.backend.domain.category.exception.CategoryException;
import com.example.backend.domain.category.repository.CategoryRepository;
import com.example.backend.domain.category.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryRequestDto categoryRequestDto;
    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        categoryRequestDto = new CategoryRequestDto(
                CategoryType.STUDY,
                "category1"
        );
        category1 = new Category("Category 1", CategoryType.EXERCISE);
        category2 = new Category("Category 2", CategoryType.HOBBY);
    }

    @Test
    @DisplayName("카테고리 생성 테스트")
    void createCategoryTest() {
        Category category = Category.builder()
                .name(categoryRequestDto.getName())
                .categoryType(categoryRequestDto.getType())
                .build();

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDto response = categoryService.create(categoryRequestDto);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(category.getName());
        assertThat(response.getType()).isEqualTo(category.getCategoryType());

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("카테고리 수정 테스트")
    void modifyCategoryTest() {
        when(categoryRepository.findById(category1.getId())).thenReturn(Optional.of(category1));

        CategoryResponseDto response = categoryService.modify(category1.getId(), categoryRequestDto);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(categoryRequestDto.getName());
        assertThat(response.getType()).isEqualTo(categoryRequestDto.getType());

        verify(categoryRepository, times(1)).findById(category1.getId());
    }

    @Test
    @DisplayName("카테고리 수정시 잘못된 id로 실패 테스트")
    void modifyFailureExceptionTest() {
        Long wrongId = 100L;

        assertThatThrownBy(() -> categoryService.modify(wrongId, categoryRequestDto))
                .isInstanceOf(CategoryException.class)
                .hasMessageContaining("해당 카테고리는 존재하지 않습니다.");

        verify(categoryRepository, times(1)).findById(wrongId);

    }

    @Test
    @DisplayName("카테고리 전체 조회 성공 테스트")
    void listCategoryTest() {
        List<Category> categories = Arrays.asList(category1, category2);
        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryResponseDto> response = categoryService.getAllCategories();

        assertThat(response).hasSize(2);
        assertThat(response.get(0).getName()).isEqualTo(category1.getName());
        assertThat(response.get(1).getName()).isEqualTo(category2.getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("카테고리가 하나도 존재하지 않을때 조회 실패 테스트")
    void emptyListCategoryTest(){
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> categoryService.getAllCategories())
                .isInstanceOf(CategoryException.class)  // 예외 타입 확인
                .hasMessageContaining("카테고리 목록이 존재하지 않습니다.");

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("카테고리 삭제 성공 테스트")
    void deleteCategoryTest() {
        when(categoryRepository.findById(category1.getId())).thenReturn(Optional.of(category1));

        categoryService.delete(category1.getId());

        verify(categoryRepository, times(1)).findById(category1.getId());
        verify(categoryRepository, times(1)).delete(category1);
    }

    @Test
    @DisplayName("카테고리 삭제 시 잘못된 id로 실패 테스트")
    void deleteCategoryFailureTest(){
        when(categoryRepository.findById(category1.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.delete(category1.getId()))
                .isInstanceOf(CategoryException.class)
                .hasMessageContaining("해당 카테고리는 존재하지 않습니다.");
        verify(categoryRepository, times(1)).findById(category1.getId());
        verify(categoryRepository, times(0)).delete(any(Category.class));
    }

}