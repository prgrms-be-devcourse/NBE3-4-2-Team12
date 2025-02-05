package com.example.backend.domain.voter.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum VoterErrorCode {
    NOT_FOUND_VOTE(HttpStatus.NOT_FOUND, "404-1", "투표를 찾을 수 없습니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "404-2", "사용자를 찾을 수 없습니다."),
    NOT_FOUND_GROUP(HttpStatus.NOT_FOUND, "404-3", "모임을 찾을 수 없습니다."),
    NOT_GROUP_MEMBER(HttpStatus.FORBIDDEN, "403-1", "사용자는 해당 모임의 멤버가 아닙니다."),
    ALREADY_VOTED(HttpStatus.CONFLICT, "409-1", "이미 참여한 투표입니다."),
    NOT_VOTED(HttpStatus.BAD_REQUEST, "400-1", "해당 투표에 참여한 기록이 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
