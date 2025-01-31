package com.example.backend.domain.category.controller;


import com.example.backend.domain.category.dto.CategoryRequestDto;
import com.example.backend.domain.category.dto.CategoryResponseDto;
import com.example.backend.domain.category.entity.CategoryType;
import com.example.backend.domain.category.service.CategoryService;
import com.example.backend.domain.group.dto.GroupRequestDto;
import com.example.backend.domain.group.dto.GroupResponseDto;
import jakarta.validation.Valid;
import jakarta.xml.ws.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto>createGroup(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        log.info("New category creation requested");
        CategoryResponseDto response = categoryService.create(categoryRequestDto);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }
}
