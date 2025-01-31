package com.example.backend.domain.group.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GroupErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,"404","해당 그룹은 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
