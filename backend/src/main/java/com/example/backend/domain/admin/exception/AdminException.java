package com.example.backend.domain.admin.exception;

import org.springframework.http.HttpStatus;

public class AdminException extends RuntimeException {
        private final AdminErrorCode adminErrorCode;
        
    public AdminException(AdminErrorCode adminErrorCode) {
        super(adminErrorCode.getMessage());
        this.adminErrorCode = adminErrorCode;
    }

    public HttpStatus getStatus() {
        return adminErrorCode.getHttpStatus();
    }

    public String getCode() {
        return adminErrorCode.getCode();
    }
}
