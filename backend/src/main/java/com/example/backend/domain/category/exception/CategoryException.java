package com.example.backend.domain.category.exception;

import org.springframework.http.HttpStatus;

public class CategoryException extends RuntimeException{

    private final CategoryErrorCode categoryErrorCode;

    public CategoryException(CategoryErrorCode categoryErrorCode) {
        super(categoryErrorCode.getMessage());
        this.categoryErrorCode = categoryErrorCode;
    }

    public HttpStatus getStatus() {
        return categoryErrorCode.getHttpStatus();
    }

    public String getCode() {
        return categoryErrorCode.getCode();
    }
}
