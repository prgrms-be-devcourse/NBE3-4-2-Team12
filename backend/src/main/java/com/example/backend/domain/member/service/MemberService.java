package com.example.backend.domain.member.service;

import org.springframework.stereotype.Service;

import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.repository.MemberRepository;
import com.example.backend.global.auth.dto.KakaoUserInfoResponseDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;

	public boolean existsByKakaoId(Long kakaoId) {

		return memberRepository.existsByKakaoId(kakaoId);
	}

	public void join(KakaoUserInfoResponseDto kakaoUserInfoDto, String refreshToken) {

		Member member = Member.of(kakaoUserInfoDto);
		member.updateRefreshToken(refreshToken);
		memberRepository.save(member);
	}
}
