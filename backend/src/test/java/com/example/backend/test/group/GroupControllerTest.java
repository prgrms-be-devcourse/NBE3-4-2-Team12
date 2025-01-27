package com.example.backend.test.group;

import com.example.backend.domain.group.dto.GroupRequestDto;
import com.example.backend.domain.group.service.GroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GroupControllerTest {

    @Autowired
    private GroupService groupService;

    @Test
    @DisplayName("그룹 생성")
    void t1(){
        groupService.create(1L,new GroupRequestDto("제목1","내용1",5));
    }

    @Test
    @DisplayName("그룹 조회")
    void t2(){
        groupService.listGroups();
    }
}
