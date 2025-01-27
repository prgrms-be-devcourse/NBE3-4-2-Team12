package com.example.backend.domain.group.controller;

import com.example.backend.domain.group.dto.GroupRequestDto;
import com.example.backend.domain.group.dto.GroupResponseDto;
import com.example.backend.domain.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/{ownerId}")
    public ResponseEntity<GroupResponseDto>createGroup(@PathVariable Long ownerId, @RequestBody GroupRequestDto requestDto) {
        GroupResponseDto response = groupService.create(ownerId, requestDto);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }
    @GetMapping
    public ResponseEntity<List<GroupResponseDto>> listGroups() {
        List<GroupResponseDto> response = groupService.listGroups();
        return new ResponseEntity<>(response,HttpStatusCode.valueOf(200));
    }
}
