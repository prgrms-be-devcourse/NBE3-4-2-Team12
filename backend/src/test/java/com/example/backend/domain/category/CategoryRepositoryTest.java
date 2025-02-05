package com.example.backend.domain.category;

import com.example.backend.domain.category.entity.Category;
import com.example.backend.domain.category.entity.CategoryType;
import com.example.backend.domain.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;


    @BeforeEach
    void setUp(){
        category = Category.builder()
                .name("Category1")
                .categoryType(CategoryType.EXERCISE)
                .build();
    }
    @Test
    @DisplayName("카테고리 저장 테스트")
    void saveTest(){
        Category savedCategory = categoryRepository.save(category);

        assertNotNull(savedCategory.getId());
        assertEquals(category.getName(),savedCategory.getName());
        assertEquals(category.getCategoryType(),savedCategory.getCategoryType());
    }
    @Test
    @DisplayName("카테고리 ID로 조회 테스트")
    void findByIdTest() {
        Category savedCategory = categoryRepository.save(category);

        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());

        assertTrue(foundCategory.isPresent());
        assertEquals(savedCategory.getName(), foundCategory.get().getName());
        assertEquals(savedCategory.getCategoryType(), foundCategory.get().getCategoryType());
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    void deleteTest() {
        Category savedCategory = categoryRepository.save(category);

        categoryRepository.delete(savedCategory);

        Optional<Category> deletedCategory = categoryRepository.findById(savedCategory.getId());
        assertTrue(deletedCategory.isEmpty());
    }

}
