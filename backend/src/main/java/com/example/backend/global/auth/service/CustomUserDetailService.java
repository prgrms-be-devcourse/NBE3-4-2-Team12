package com.example.backend.global.auth.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.backend.domain.member.dto.MemberInfoDto;
import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.service.MemberService;
import com.example.backend.global.auth.model.CustomUserDetails;
import com.example.backend.global.auth.util.JwtUtil;

import lombok.RequiredArgsConstructor;

/**
 * CustomUserDetailService
 * <p></p>
 * @author 100minha
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

	private final MemberService memberService;
	private final JwtUtil jwtUtil;

	@Override
	public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Member member = memberService.findById(Long.valueOf(username));
		return new CustomUserDetails(MemberInfoDto.of(member));
	}

	public CustomUserDetails loadUserByAccessToken(String accessToken) {

		return new CustomUserDetails(jwtUtil.getMemberInfoDto(accessToken));
	}
}
