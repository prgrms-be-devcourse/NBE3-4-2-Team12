package com.example.backend.domain.admin.controller;

import com.example.backend.domain.admin.dto.AdminLoginRequest;
import com.example.backend.domain.admin.entity.Admin;
import com.example.backend.domain.admin.service.AdminService;
import com.example.backend.domain.group.service.GroupService;
import com.example.backend.global.auth.service.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminService adminService;
    private final GroupService groupService;

    // 관리자 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AdminLoginRequest request, HttpServletResponse response) {
        Admin admin = adminService.getAdmin(request.getAdminName(), request.getPassword());

        this.adminService.generateToken(admin, response);
        this.adminService.generateAndSaveRefreshToken(admin, response);

        return ResponseEntity.ok("로그인 성공");
    }

    // 관리자 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response, HttpServletRequest request) {
        this.adminService.logout(response, request);
        return ResponseEntity.ok("로그아웃 성공");
    }

    // 게시물 삭제
    @DeleteMapping("/group/{groupId}")
    public ResponseEntity<String> deleteGroup(HttpServletResponse response, @PathVariable("groupId") Long id) {
        this.groupService.deleteGroup(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
