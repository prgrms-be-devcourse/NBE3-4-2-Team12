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
    public GroupResponseDto create(Long ownerId, GroupRequestDto groupRequestDto){
        Group group = Group.builder()
                .title(groupRequestDto.getTitle())
                .description(groupRequestDto.getDescription())
                .status(GroupStatus.ACTIVE)
                .ownerId(ownerId)
                .maxParticipants(groupRequestDto.getMaxParticipants())
                .build();
        groupRepository.save(group);
        return new GroupResponseDto(group);
    }

    @Transactional
    public List<GroupResponseDto> findAllGroups() {
        return groupRepository.findAll().stream().map(GroupResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public void deleteGroup(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(()-> new GroupException(GroupErrorCode.NOT_FOUND));
        group.setStatus(GroupStatus.DELETED);
        groupRepository.save(group);
    }

    @Transactional
    public GroupResponseDto modifyGroup(Long ownerId, GroupModifyRequestDto groupModifyRequestDto) {
        List<Group> groups = groupRepository.findByOwnerId(ownerId).orElseThrow(()-> new GroupException(GroupErrorCode.NOT_FOUND));
        GroupResponseDto groupResponseDto = new GroupResponseDto();
        for(Group group : groups){
            if(group.getId() == groupModifyRequestDto.getGroupId()){
                group.update(
                        groupModifyRequestDto.getTitle(),
                        groupModifyRequestDto.getDescription(),
                        groupModifyRequestDto.getMaxParticipants()
                );
                groupRepository.save(group);
                groupResponseDto = new GroupResponseDto(group);
                break;
            }
        }
        if(groupResponseDto == null){
            throw new GroupException(GroupErrorCode.NOT_FOUND);
        }
        return groupResponseDto;
    }
}

