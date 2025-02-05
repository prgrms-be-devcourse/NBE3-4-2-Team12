package com.example.backend.test.group;

import com.example.backend.domain.group.controller.GroupController;
import com.example.backend.domain.group.dto.GroupRequestDto;
import com.example.backend.domain.group.dto.GroupResponseDto;
import com.example.backend.domain.group.entity.GroupStatus;
import com.example.backend.domain.group.service.GroupService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GroupControllerTest {

    @Autowired
    private GroupService groupService;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("그룹 생성")
    void t1() throws Exception {
        ResultActions resultActions = mvc.perform(
                post("/groups")
                        .content("""
                                {
                                  "title": "제목1",
                                  "description": "내용1",
                                  "memberId":1,
                                  "maxParticipants":5,
                                  "categoryIds": [1],
                                  "status":"RECRUITING"
                                }
                                """)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        ).andDo(print());

        GroupResponseDto groupResponseDto = groupService.create(new GroupRequestDto("제목1","내용1",5,1L, Arrays.asList(1L), GroupStatus.RECRUITING));
        System.out.println("Generated Group ID: " + groupResponseDto.getId());
        resultActions.andExpect(handler().handlerType(GroupController.class))
                .andExpect(handler().methodName("createGroup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(groupResponseDto.getTitle()))
                .andExpect(jsonPath("$.description").value(groupResponseDto.getDescription()))
                .andExpect(jsonPath("$.memberId").value(groupResponseDto.getMemberId()))
                .andExpect(jsonPath("$.maxParticipants").value(groupResponseDto.getMaxParticipants()))
                .andExpect(jsonPath("$.category").isArray())
                .andExpect(jsonPath("$.category[0].id").value(1L))
                .andExpect(jsonPath("$.status").value(Matchers.is("RECRUITING")));
    }

    @Test
    @DisplayName("모임 전체 조회")
    void t2() {
        groupService.findAllGroups();
    }
}
