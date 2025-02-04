package com.example.backend.domain.voter.exception;

public enum VoterErrorCode {
    NOT_FOUND_VOTE("투표를 찾을 수 없습니다."),
    NOT_FOUND_MEMBER("사용자를 찾을 수 없습니다."),
    NOT_FOUND_GROUP("모임을 찾을 수 없습니다."),
    NOT_GROUP_MEMBER("사용자는 해당 모임의 멤버가 아닙니다."),
    ALREADY_VOTED("이미 참여한 투표입니다.");

    private final String message;

    VoterErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
