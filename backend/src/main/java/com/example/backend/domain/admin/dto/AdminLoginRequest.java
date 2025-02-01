package com.example.backend.domain.admin.dto;

import lombok.Getter;

@Getter
public class AdminLoginRequest {
    private String adminName;
    private String password;
}
