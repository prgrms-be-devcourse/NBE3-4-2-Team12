package com.example.backend.domain.admin.controller;

import com.example.backend.domain.admin.dto.AdminLoginRequest;
import com.example.backend.domain.admin.dto.AdminLoginResponseDto;
import com.example.backend.domain.admin.entity.Admin;
import com.example.backend.domain.admin.service.AdminService;
import com.example.backend.global.auth.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminService adminService;
    private final JwtProvider jwtProvider;

    // 관리자 로그인
    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponseDto> login(@RequestBody AdminLoginRequest request) {
        Admin admin = adminService.getAdmin(request.getAdminName(), request.getPassword());

        String accessToken = this.jwtProvider.generateToken(admin);
        String refreshToken = this.jwtProvider.generateRefreshToken(admin);

        return ResponseEntity.ok(new AdminLoginResponseDto(accessToken));
    }

    // 관리자 정보 조회
    @GetMapping("/info")
    public ResponseEntity<String> getAdminInfo(@RequestHeader("Authorization") String token) {
        if(token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT 토큰이 필요합니다.");
        }

        // Bearer 제거 후 순수 토큰 추출
        token = token.substring(7).trim();

        // JWT 검증 후 관리자 정보 추출
        Claims claims;
        try {
            claims = jwtProvider.parseToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        String adminName = claims.getSubject();
        String role = claims.get("role", String.class);

        // 관리자 권한 체크
        if(!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }

        // 정상 인증되면 관리자 정보 반환
        return ResponseEntity.ok("관리자 계정: " + adminName);
    }

}
