package com.example.backend.domain.voter.exception;

public class VoterException extends RuntimeException {
    private final VoterErrorCode errorCode;

    public VoterException(VoterErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public VoterErrorCode getErrorCode() {
        return errorCode;
    }
}
