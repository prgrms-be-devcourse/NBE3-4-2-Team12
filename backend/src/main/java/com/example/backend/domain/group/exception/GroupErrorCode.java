package com.example.backend.domain.group.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GroupErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,"404","해당 그룹은 존재하지 않습니다."),
    NOT_FOUND_LIST(HttpStatus.NOT_FOUND,"404","그룹 목록이 존재하지 않습니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND,"404","해당 회원은 존재하지 않습니다."),
    ALREADY_DELETED(HttpStatus.BAD_REQUEST,"404","이미 삭제된 그룹입니다."),
    EXISTED_MEMBER(HttpStatus.CONFLICT,"409","이미 가입된 회원입니다."),
    OVER_MEMBER(HttpStatus.CONFLICT,"409","그룹 인원이 가득 찼습니다."),
    NOT_RECRUITING(HttpStatus.CONFLICT,"409","모집중인 그룹이 아닙니다."),
    COMPLETED(HttpStatus.CONFLICT,"409","모집이 완료된 그룹입니다."),
    VOTING(HttpStatus.FORBIDDEN,"403","투표중인 그룹은 참여할 수 없습니다."),
    INCORRECT(HttpStatus.NOT_FOUND,"404","잘못된 그룹 상태 값입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"401","인증되지 않은 사용자입니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
