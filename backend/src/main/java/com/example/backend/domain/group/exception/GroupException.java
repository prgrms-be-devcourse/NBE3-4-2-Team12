package com.example.backend.domain.group.exception;

import org.springframework.http.HttpStatus;

public class GroupException extends RuntimeException{
    private final GroupErrorCode groupErrorCode;

    public GroupException(GroupErrorCode groupErrorCode) {
        super(groupErrorCode.getMessage());
        this.groupErrorCode = groupErrorCode;
    }

    public HttpStatus getStatus() {
        return groupErrorCode.getHttpStatus();
    }

    public String getCode() {
        return groupErrorCode.getCode();
    }
}
