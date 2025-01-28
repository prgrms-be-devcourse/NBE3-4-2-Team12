package com.example.backend.domain.group.controller;

import com.example.backend.domain.group.dto.GroupModifyRequestDto;
import com.example.backend.domain.group.dto.GroupRequestDto;
import com.example.backend.domain.group.dto.GroupResponseDto;
import com.example.backend.domain.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        log.info("New group creation requested");
        GroupResponseDto response = groupService.create(ownerId, requestDto);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @GetMapping
    public ResponseEntity<List<GroupResponseDto>> listGroups(){
        log.info("Listing all groups are requested");
        List<GroupResponseDto> response = groupService.findAllGroups();
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/{ownerId}")
    public ResponseEntity<GroupResponseDto> deleteGroup(@PathVariable("ownerId") Long id){
        log.info("Deleting a particular group is being requested");
        groupService.deleteGroup(id);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    @PutMapping("/{ownerId}")
    public ResponseEntity<GroupResponseDto>modifyGroup(@PathVariable Long ownerId, @RequestBody GroupModifyRequestDto modifyRequestDto){
        log.info("Modifying a particular group is being requested");
        GroupResponseDto response = groupService.modifyGroup(ownerId,modifyRequestDto);
        return new ResponseEntity<>(response,HttpStatus.valueOf(200));
    }
}
