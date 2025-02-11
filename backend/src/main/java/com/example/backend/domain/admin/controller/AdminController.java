package com.example.backend.domain.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.admin.dto.AdminLoginRequest;
import com.example.backend.domain.admin.entity.Admin;
import com.example.backend.domain.admin.service.AdminService;
import com.example.backend.domain.group.service.GroupService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

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

	@GetMapping
	public ResponseEntity<String> isAdmin() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String principal = authentication.getName();
		if (authentication.isAuthenticated() && principal.equals("anonymousUser")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body("인증되지 않은 관리자입니다.");
		}

		return ResponseEntity.ok().body("관리자 인증되었습니다.");
	}
}
