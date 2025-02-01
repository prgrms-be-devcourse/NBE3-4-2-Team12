package com.example.backend.domain.admin.controller;

import com.example.backend.domain.admin.dto.AdminLoginRequest;
import com.example.backend.domain.admin.dto.AdminLoginResponseDto;
import com.example.backend.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponseDto> login(@RequestBody AdminLoginRequest request) {
        String token = adminService.login(request.getAdminName(), request.getPassword());
        return ResponseEntity.ok(new AdminLoginResponseDto(token));
    }
}
