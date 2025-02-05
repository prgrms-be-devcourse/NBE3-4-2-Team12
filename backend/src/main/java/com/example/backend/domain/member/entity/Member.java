package com.example.backend.domain.member.entity;

import com.example.backend.global.auth.dto.KakaoUserInfoResponseDto;
import com.example.backend.global.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private Long kakaoId;

	private String email;

	private String nickname;

	@Column(name = "kakao_access_token")
	private String kakaoAccessToken;        // 카카오에서 발급한 액세스 토큰

	@Column(name = "kakao_refresh_token")
	private String kakaoRefreshToken;    // 카카오에서 발급한 리프레시 토큰

	@Builder
	public Member(Long kakaoId, String email, String nickname,
		String kakaoAccessToken, String kakaoRefreshToken) {

		this.kakaoId = kakaoId;
		this.email = email;
		this.nickname = nickname;
		this.kakaoAccessToken = kakaoAccessToken;
		this.kakaoRefreshToken = kakaoRefreshToken;
	}

	public static Member of(KakaoUserInfoResponseDto kakaoUserInfoDto) {

		return Member.builder()
			.kakaoId(kakaoUserInfoDto.id())
			.email(kakaoUserInfoDto.kakaoAccount().email())
			.nickname(kakaoUserInfoDto.properties().nickname())
			.build();
	}

	public void updateAccessToken(String accessToken) {
		this.kakaoAccessToken = accessToken;
	}

	public void updateRefreshToken(String refreshToken) {
		this.kakaoRefreshToken = refreshToken;
	}
}
