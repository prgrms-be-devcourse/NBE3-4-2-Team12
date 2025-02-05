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

	public void join(KakaoUserInfoResponseDto kakaoUserInfoDto,
		String kakaoAccessToken, String kakaoRefreshToken) {

		Member member = Member.of(kakaoUserInfoDto);
		member.updateAccessToken(kakaoAccessToken);
		member.updateRefreshToken(kakaoRefreshToken);
		memberRepository.save(member);
	}
}
