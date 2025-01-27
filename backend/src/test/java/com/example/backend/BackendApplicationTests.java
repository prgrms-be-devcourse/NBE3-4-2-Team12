package com.example.backend;

import com.example.backend.domain.group.dto.GroupRequestDto;
import com.example.backend.domain.group.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {

	@Autowired
	private GroupService groupService;

	@Test
	void contextLoads() {
		groupService.create(1L,new GroupRequestDto("제목1","내용1",5));
	}

}
