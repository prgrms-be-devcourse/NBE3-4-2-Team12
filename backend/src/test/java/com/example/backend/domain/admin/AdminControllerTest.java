package com.example.backend.domain.admin;

import com.example.backend.domain.admin.entity.Admin;
import com.example.backend.domain.admin.repository.AdminRepository;
import com.example.backend.domain.group.entity.Group;
import com.example.backend.domain.group.entity.GroupStatus;
import com.example.backend.domain.group.repository.GroupRepository;
import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MemberRepository memberRepository;

    private ResultActions loginAndGetResponse() throws Exception {
        String loginRequestJson = """
                {
                    "adminName": "admin",
                    "password": "1234"
                }
                """;

        ResultActions loginResponse = mockMvc.perform(post("/admin/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestJson));

        return loginResponse;
    }

    private Long createGroupResponse() throws Exception {
        Member member = Member.builder()
                .nickname("testUser")
                .email("test@test.com")
                .build();
        this.memberRepository.save(member);

        Group group = Group.builder()
                .title("운동모임")
                .description("운동 하는 사람들의 모임")
                .member(member)
                .status(GroupStatus.RECRUITING)
                .maxParticipants(10)
                .build();
        groupRepository.save(group);

        return group.getId();
    }

    @BeforeAll
    static void setUp(@Autowired AdminRepository adminRepository) {
        Admin admin = Admin.builder()
                .adminName("admin")
                .password("$2a$12$wS8w9vGzZ345XlGazbp8mekCkPyKoPFbky96pr0EqW.6I0Xtdt.YO")
                .build();

        adminRepository.save(admin);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccessTest() throws Exception {
        ResultActions loginResponse = loginAndGetResponse();

            loginResponse
                .andExpect(status().isOk())
                // 쿠키 검증으로 변경
                .andExpect(cookie().exists("accessToken")) // accessToken 쿠키 존재 여부 확인
                .andExpect(cookie().exists("refreshToken")) // refreshToken 쿠키 존재 여부 확인
                .andExpect(cookie().httpOnly("accessToken", true)) // JavaScript 접근 불가
                .andExpect(cookie().httpOnly("refreshToken", true)) // JavaScript 접근 불가
                .andExpect(cookie().secure("accessToken", true)) // HTTPS에서만 전송
                .andExpect(cookie().secure("refreshToken", true)) // HTTPS에서만 전송
                .andReturn();
    }

    @Test
    @DisplayName("로그아웃 성공 테스트")
    void logoutSuccessTest() throws Exception {
        ResultActions loginResponse = loginAndGetResponse();

        loginResponse.andExpect(status().isOk());

        String accessToken = loginResponse.andReturn().getResponse().getCookie("accessToken").getValue();
        String refreshToken = loginResponse.andReturn().getResponse().getCookie("refreshToken").getValue();

        ResultActions logoutResponse = mockMvc.perform(post("/admin/logout")
                .cookie(new Cookie("accessToken", accessToken))
                .cookie(new Cookie("refreshToken", refreshToken)));

        logoutResponse
                .andExpect(status().isOk())
                .andExpect(cookie().value("accessToken", ""))
                .andExpect(cookie().value("refreshToken", ""));
    }

    @Test
    @DisplayName("관리자 그룹 삭제 테스트")
    void DeleteAdmin() throws Exception {
        Long groupId = createGroupResponse();

        ResultActions loginResponse = loginAndGetResponse();
        loginResponse.andExpect(status().isOk());

        String accessToken = loginResponse.andReturn().getResponse().getCookie("accessToken").getValue();
        String refreshToken = loginResponse.andReturn().getResponse().getCookie("refreshToken").getValue();

        ResultActions deleteGroupResponse = mockMvc.perform(delete("/admin/group/" + groupId)
                .cookie(new Cookie("accessToken", accessToken))
                .cookie(new Cookie("refreshToken", refreshToken)));

        deleteGroupResponse
                .andExpect(status().isOk());
    }
}

