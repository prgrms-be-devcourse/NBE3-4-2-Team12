package com.example.backend.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.domain.member.dto.MemberResponseDto;
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

	@Transactional(readOnly = true)
	public MemberResponseDto findMemberResponseDtoById(Long id) {

		return memberRepository.findMemberResponseDtoById(id).orElseThrow(() ->
			new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
	}

	@Transactional
	public void join(KakaoUserInfoResponseDto kakaoUserInfoDto) {

		memberRepository.save(Member.of(kakaoUserInfoDto));
	}

	@Transactional(readOnly = true)
	public Member findByKakaoRefreshToken(String refreshToken) {

		return memberRepository.findByKakaoRefreshToken(refreshToken).orElseThrow(() ->
			new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
	}
}
