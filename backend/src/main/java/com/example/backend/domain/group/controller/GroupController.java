package com.example.backend.domain.group.controller;

import com.example.backend.domain.group.dto.GroupModifyRequestDto;
import com.example.backend.domain.group.dto.GroupRequestDto;
import com.example.backend.domain.group.dto.GroupResponseDto;
import com.example.backend.domain.group.dto.JoinGroupRequestDto;
import com.example.backend.domain.group.service.GroupService;
import com.example.backend.global.auth.model.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupResponseDto> createGroup(@RequestBody @Valid GroupRequestDto requestDto,
                                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("New group creation requested");
        Long memberId = customUserDetails.getUserId();
        GroupResponseDto response = groupService.create(requestDto, memberId);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @GetMapping
    public ResponseEntity<List<GroupResponseDto>> listGroups(){
        log.info("Listing all groups are requested");
        List<GroupResponseDto> response = groupService.findNotDeletedAllGroups();
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponseDto> getGroup(@PathVariable("groupId") Long id){
        log.info("Getting group by groupId {}", id);
        GroupResponseDto response = groupService.findGroup(id);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<GroupResponseDto> deleteGroup(@PathVariable("groupId") Long id){
        log.info("Deleting a particular group is being requested");
        groupService.deleteGroup(id);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<GroupResponseDto>modifyGroup(@PathVariable("groupId") Long groupId, @RequestBody @Valid GroupModifyRequestDto modifyRequestDto){
        log.info("Modifying a particular group is being requested");
        GroupResponseDto response = groupService.modifyGroup(groupId,modifyRequestDto);
        return new ResponseEntity<>(response,HttpStatus.valueOf(200));
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinGroup(@RequestBody @Valid JoinGroupRequestDto joinGroupRequestDto,
                                            @AuthenticationPrincipal CustomUserDetails userDetails){
        Long groupId = joinGroupRequestDto.getGroupId();
        Long memberId = userDetails.getUserId();

        if (memberId == null || groupId == null) {
            return ResponseEntity.badRequest().body("그룹 또는 회원의 데이터가 없습니다.");
        }

        groupService.joinGroup(groupId, memberId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("member")
    public ResponseEntity<List<GroupResponseDto>> getGroupByMember(@AuthenticationPrincipal CustomUserDetails userDetails){
        List<GroupResponseDto> response =  groupService.getGroupByMemberId(userDetails.getUserId());
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }
}
