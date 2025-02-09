package com.example.backend.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.member.dto.MemberInfoDto;
import com.example.backend.domain.member.dto.MemberModifyRequestDto;
import com.example.backend.domain.member.service.MemberService;
import com.example.backend.global.auth.model.CustomUserDetails;
import com.example.backend.global.response.ApiResponse;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

	private final MemberService memberService;

	@GetMapping
	public ResponseEntity<ApiResponse<MemberInfoDto>> findMember(
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {

		MemberInfoDto memberDto = MemberInfoDto.builder()
			.id(customUserDetails.getUserId())
			.nickname(customUserDetails.getUsername())
			.email(customUserDetails.getEmail())
			.build();
		return ResponseEntity.ok().body(ApiResponse.of(memberDto));
	}

	@PutMapping
	public ResponseEntity<ApiResponse<MemberInfoDto>> modify(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@Valid @RequestBody MemberModifyRequestDto memberModifyDto,
		HttpServletResponse response
	) {

		MemberInfoDto memberInfoDto = memberService.modify(customUserDetails.getUserId(), memberModifyDto, response);
		return ResponseEntity.ok().body(ApiResponse.of(memberInfoDto));
	}
}
