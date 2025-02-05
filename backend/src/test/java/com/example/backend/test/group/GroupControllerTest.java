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
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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
    @DisplayName("그룹 전체 조회")
    void t2() throws Exception {
        ResultActions resultActions = mvc.perform(
                get("/groups")
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        ).andDo(print());

        resultActions.andExpect(handler().handlerType(GroupController.class))
                .andExpect(handler().methodName("listGroups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(Matchers.greaterThan(0)));
    }

    @Test
    @DisplayName("그룹 특정 조회")
    void t3() throws Exception {
        ResultActions resultActions = mvc.perform(
                get("/groups/{id}",1)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        ).andDo(print());

        resultActions.andExpect(handler().handlerType(GroupController.class))
                .andExpect(handler().methodName("getGroup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.description").isNotEmpty())
                .andExpect(jsonPath("$.memberId").isNotEmpty())
                .andExpect(jsonPath("$.maxParticipants").isNotEmpty())
                .andExpect(jsonPath("$.category").isNotEmpty())
                .andExpect(jsonPath("$.status").isNotEmpty());
    }

    @Test
    @DisplayName("그룹 수정")
    void t4() throws Exception {
        ResultActions resultActions = mvc.perform(
                put("/groups/{id}",1L)
                        .content("""
                                {
                                  "title": "제목2",
                                  "description": "내용3",
                                  "maxParticipants":6
                                }
                                """)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        ).andDo(print());

        resultActions.andExpect(handler().handlerType(GroupController.class))
                .andExpect(handler().methodName("modifyGroup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("제목2"))
                .andExpect(jsonPath("$.description").value("내용3"))
                .andExpect(jsonPath("$.maxParticipants").value(6));
    }

    @Test
    @DisplayName("그룹 삭제")
    void t5() throws Exception {
        ResultActions resultActions = mvc.perform(
                delete("/groups/{id}",3)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        ).andDo(print());

        resultActions.andExpect(handler().handlerType(GroupController.class))
                .andExpect(handler().methodName("deleteGroup"))
                .andExpect(status().isOk());
    }
}
