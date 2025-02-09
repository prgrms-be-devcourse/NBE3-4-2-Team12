package com.example.backend.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.domain.member.dto.MemberInfoDto;
import com.example.backend.domain.member.dto.MemberModifyRequestDto;
import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.exception.MemberErrorCode;
import com.example.backend.domain.member.exception.MemberException;
import com.example.backend.domain.member.repository.MemberRepository;
import com.example.backend.global.auth.dto.KakaoUserInfoResponseDto;
import com.example.backend.global.auth.service.CookieService;
import com.example.backend.global.auth.util.TokenProvider;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final TokenProvider tokenProvider;
	private final CookieService cookieService;

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
	public MemberInfoDto findMemberInfoDtoById(Long id) {

		return memberRepository.findMemberInfoDtoById(id).orElseThrow(() ->
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

	@Transactional
	public MemberInfoDto modify(Long id, MemberModifyRequestDto memberModifyDto,
		HttpServletResponse response) {

		Member member = findById(id);
		member.modify(memberModifyDto);
		// 사용자 정보 수정 후 수정된 정보로 액세스 토큰 재발급
		String reissuedAccessToken = tokenProvider.generateMemberAccessToken(
			member.getId(), member.getNickname(), member.getEmail());

		cookieService.addAccessTokenToCookie(reissuedAccessToken, response);
		return MemberInfoDto.of(member);
	}
}
