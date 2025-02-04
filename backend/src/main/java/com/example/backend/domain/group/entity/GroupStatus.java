package com.example.backend.domain.group.entity;

import com.example.backend.domain.group.exception.GroupErrorCode;
import com.example.backend.domain.group.exception.GroupException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum GroupStatus {
    RECRUITING, // 모집 중
    NOT_RECRUITING, // 모집 중단
    COMPLETED, // 모집 완료
    VOTING, // 투표중
    DELETED; // 삭제

    @JsonCreator
    public static GroupStatus from(String value) {
        for (GroupStatus status : GroupStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new GroupException(GroupErrorCode.INCORRECT);
    }
}
