package com.example.backend.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.exception.MemberErrorCode;
import com.example.backend.domain.member.exception.MemberException;
import com.example.backend.domain.member.repository.MemberRepository;
import com.example.backend.global.auth.dto.KakaoUserInfoResponseDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public Member findById(Long id) {

		return memberRepository.findById(id).orElseThrow(() ->
			new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public Member findByKakaoId(Long kakaoId) {

		return memberRepository.findByKakaoId(kakaoId).orElseThrow(() ->
			new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public boolean existsByKakaoId(Long kakaoId) {

		return memberRepository.existsByKakaoId(kakaoId);
	}

	@Transactional
	public void join(KakaoUserInfoResponseDto kakaoUserInfoDto) {

		Member member = Member.of(kakaoUserInfoDto);
		Member savedMember = memberRepository.save(member);
	}

	@Transactional
	public void updateAccessToken(Member member, String accessToken) {

		member.updateAccessToken(accessToken);
	}

	@Transactional
	public void updateRefreshToken(Member member, String refreshToken) {

		member.updateRefreshToken(refreshToken);
	}
}
