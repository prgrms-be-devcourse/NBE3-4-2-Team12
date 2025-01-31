package com.example.backend.domain.group.service;

import com.example.backend.domain.group.dto.GroupModifyRequestDto;
import com.example.backend.domain.group.dto.GroupRequestDto;
import com.example.backend.domain.group.dto.GroupResponseDto;
import com.example.backend.domain.group.entity.Group;
import com.example.backend.domain.group.entity.GroupStatus;
import com.example.backend.domain.group.exception.GroupErrorCode;
import com.example.backend.domain.group.exception.GroupException;
import com.example.backend.domain.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;

    @Transactional
    public GroupResponseDto create(GroupRequestDto groupRequestDto){
        Group group = Group.builder()
                .title(groupRequestDto.getTitle())
                .description(groupRequestDto.getDescription())
                .status(GroupStatus.ACTIVE)
                .maxParticipants(groupRequestDto.getMaxParticipants())
                .build();
        groupRepository.save(group);
        return new GroupResponseDto(group);
    }

    @Transactional(readOnly = true)
    public List<GroupResponseDto> findAllGroups() {
        return groupRepository.findAll().stream().map(GroupResponseDto::new).collect(Collectors.toList());
    }


    @Transactional
    public void deleteGroup(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(()-> new GroupException(GroupErrorCode.NOT_FOUND));
        checkValidity(group.getStatus());
        group.updateStatus(GroupStatus.DELETED);
        groupRepository.save(group);
    }

    @Transactional
    public GroupResponseDto modifyGroup(Long id, GroupModifyRequestDto groupModifyRequestDto) {
        Group group = groupRepository.findById(id).orElseThrow(()-> new GroupException(GroupErrorCode.NOT_FOUND));
        checkValidity(group.getStatus());
        group.update(
                groupModifyRequestDto.getTitle(),
                groupModifyRequestDto.getDescription(),
                groupModifyRequestDto.getMaxParticipants()
        );
        groupRepository.save(group);
        return new GroupResponseDto(group);
    }
    public void checkValidity(GroupStatus groupStatus){
        if (groupStatus == GroupStatus.DELETED){
            throw new GroupException(GroupErrorCode.ALREADY_DELETED);
        }
    }
}

