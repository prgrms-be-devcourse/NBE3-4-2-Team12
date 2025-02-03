package com.example.backend.domain.category.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CategoryErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,"404","해당 카테고리는 존재하지 않습니다."),
    NOT_FOUND_LIST(HttpStatus.NOT_FOUND,"404","카테고리 목록이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
