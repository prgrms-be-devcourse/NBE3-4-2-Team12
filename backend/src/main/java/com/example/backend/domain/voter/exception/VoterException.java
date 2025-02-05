package com.example.backend.domain.voter.exception;

import org.springframework.http.HttpStatus;

public class VoterException extends RuntimeException {
    private final VoterErrorCode voterErrorCode;

    public VoterException(VoterErrorCode voterErrorCode) {
        super(voterErrorCode.getMessage());
        this.voterErrorCode = voterErrorCode;
    }

    public HttpStatus getStatus() {
        return voterErrorCode.getHttpStatus();
    }

    public String getCode() {
        return voterErrorCode.getCode();
    }
}