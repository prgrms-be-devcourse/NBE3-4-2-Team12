package com.example.backend.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.member.dto.MemberResponseDto;
import com.example.backend.domain.member.service.MemberService;
import com.example.backend.global.auth.model.CustomUserDetails;
import com.example.backend.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

	private final MemberService memberService;

	@GetMapping
	public ResponseEntity<ApiResponse<MemberResponseDto>> findMember(
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		Long id = customUserDetails.getUserId();

		MemberResponseDto memberDto = memberService.findMemberResponseDtoById(id);
		return ResponseEntity.ok().body(ApiResponse.of(memberDto));
	}
}
