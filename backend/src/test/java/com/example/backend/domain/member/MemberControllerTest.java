package com.example.backend.domain.member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.domain.member.controller.MemberController;
import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.repository.MemberRepository;
import com.example.backend.domain.member.service.MemberService;
import com.example.backend.global.util.TestTokenProvider;

import jakarta.servlet.http.Cookie;

/**
 * MemberControllerTest
 * <p></p>
 * @author 100minha
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MemberControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MemberService memberService;

	/*
	테스트용 사용자 정보를 쿠키에서 조회하기 위한 데이터 설정
	테스트용 엑세스토큰까지 생성 및 api 요청 시 쿠키에 담아서 요청해야함
	 */
	private static String accessToken;

	@BeforeAll
	static void setUp(@Autowired TestTokenProvider tokenProvider, @Autowired MemberRepository memberRepository) {
		Member member = Member.builder()
			.nickname("testUser")
			.email("test@test.com")
			.build();

		memberRepository.save(member);

		accessToken = tokenProvider.generateMemberAccessToken(
			member.getId(), member.getNickname(), member.getEmail());
	}

	@Test
	@DisplayName("로그인된 사용자 정보 조회 테스트")
	void getCurrentMemberTest() throws Exception {
		// given
		Cookie accessTokenCookie = new Cookie("accessToken", accessToken);

		// when
		ResultActions resultActions = mockMvc.perform(get("/members")
			.cookie(accessTokenCookie));

		//then
		resultActions
			.andExpect(handler().handlerType(MemberController.class))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.id").value(1))
			.andExpect(jsonPath("$.data.nickname").value("testUser"))
			.andExpect(jsonPath("$.data.email").value("test@test.com"));
	}

	@Test
	@DisplayName("로그인 되지 않은 사용자 정보 조회 실패 테스트")
	void getCurrentMemberWhenNoCookieTest() throws Exception {
		// given

		// when
		ResultActions resultActions = mockMvc.perform(get("/members"));

		//then
		resultActions
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.message").value("인증에 실패했습니다."))
			.andExpect(jsonPath("$.code").value("401-1"));
	}

	@Test
	@DisplayName("사용자 정보 수정 성공 테스트")
	void modifyTest() throws Exception {
		// given
		Cookie accessTokenCookie = new Cookie("accessToken", accessToken);

		// when
		ResultActions resultActions = mockMvc.perform(put("/members")
			.content("""
				{
					"nickname" : "modified"
				}
				""")
			.contentType(MediaType.APPLICATION_JSON)
			.cookie(accessTokenCookie));

		//then
		resultActions
			.andExpect(handler().handlerType(MemberController.class))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.id").value(1))
			.andExpect(jsonPath("$.data.nickname").value("modified"))
			.andExpect(jsonPath("$.data.email").value("test@test.com"));
	}

	@Test
	@DisplayName("사용자 정보 수정 실패(nickname 공백) 테스트")
	void modifyFailWhenBlankNicknameTest() throws Exception {
		// given
		Cookie accessTokenCookie = new Cookie("accessToken", accessToken);

		// when
		ResultActions resultActions = mockMvc.perform(put("/members")
			.content("""
				{
					"nickname" : ""
				}
				""")
			.contentType(MediaType.APPLICATION_JSON)
			.cookie(accessTokenCookie));

		//then
		resultActions
			.andExpect(handler().handlerType(MemberController.class))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("입력된 객체가 유효하지 않습니다"))
			.andExpect(jsonPath("$.code").value("400-1"))
			.andExpect(jsonPath("$.errors[0].field").value("nickname"))
			.andExpect(jsonPath("$.errors[0].message").value("닉네임은 최소 2글자 이상, 10글자 이하이어야 합니다."));
	}

	@Test
	@DisplayName("사용자 정보 수정 실패(nickname 길이 최대치 초과) 테스트")
	void modifyFailWhenTooLongNicknameTest() throws Exception {
		// given
		Cookie accessTokenCookie = new Cookie("accessToken", accessToken);

		// when
		ResultActions resultActions = mockMvc.perform(put("/members")
			.content("""
				{
					"nickname" : "too_Long_Nickname"
				}
				""")
			.contentType(MediaType.APPLICATION_JSON)
			.cookie(accessTokenCookie));

		//then
		resultActions
			.andExpect(handler().handlerType(MemberController.class))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("입력된 객체가 유효하지 않습니다"))
			.andExpect(jsonPath("$.code").value("400-1"))
			.andExpect(jsonPath("$.errors[0].field").value("nickname"))
			.andExpect(jsonPath("$.errors[0].message").value("닉네임은 최소 2글자 이상, 10글자 이하이어야 합니다."));
	}

	@Test
	@DisplayName("사용자 정보 수정 실패(로그인되지 않은 상태) 테스트")
	void modifyFailWhenNoCookieTest() throws Exception {
		// given

		// when
		ResultActions resultActions = mockMvc.perform(put("/members")
			.content("""
				{
					"nickname" : "modified"
				}
				""")
			.contentType(MediaType.APPLICATION_JSON));

		//then
		resultActions
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.message").value("인증에 실패했습니다."))
			.andExpect(jsonPath("$.code").value("401-1"));
	}
}
