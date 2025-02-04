package com.example.backend.domain.admin.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum AdminErrorCode {
    NOT_FOUND_ADMIN(HttpStatus.NOT_FOUND, "404", "존재하지 않는 관리자 입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "401", "비밀번호가 올바르지않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
