package com.example.backend.domain.admin.controller;

import com.example.backend.domain.admin.dto.AdminLoginRequest;
import com.example.backend.domain.admin.entity.Admin;
import com.example.backend.domain.admin.service.AdminService;
import com.example.backend.global.auth.service.CookieService;
import jakarta.servlet.http.HttpServletResponse;
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
    private final CookieService cookieService;

    // 관리자 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AdminLoginRequest request, HttpServletResponse response) {
        Admin admin = adminService.getAdmin(request.getAdminName(), request.getPassword());

        String accessToken = this.adminService.generateToken(admin);
        String refreshToken = this.adminService.generateAndSaveRefreshToken(admin);

        cookieService.addAccessTokenToCookie(accessToken, response);
        cookieService.addRefreshTokenToCookie(refreshToken, response);

        return ResponseEntity.ok("로그인 성공");
    }

}
