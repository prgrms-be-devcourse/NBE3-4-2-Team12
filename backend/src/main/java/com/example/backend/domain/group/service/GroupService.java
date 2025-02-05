package com.example.backend.domain.group.service;

import com.example.backend.domain.category.entity.Category;
import com.example.backend.domain.category.repository.CategoryRepository;
import com.example.backend.domain.group.dto.GroupModifyRequestDto;
import com.example.backend.domain.group.dto.GroupRequestDto;
import com.example.backend.domain.group.dto.GroupResponseDto;
import com.example.backend.domain.group.entity.Group;
import com.example.backend.domain.group.entity.GroupStatus;
import com.example.backend.domain.group.exception.GroupErrorCode;
import com.example.backend.domain.group.exception.GroupException;
import com.example.backend.domain.group.repository.GroupRepository;
import com.example.backend.domain.groupcategory.GroupCategory;
import com.example.backend.domain.groupmember.entity.GroupMember;
import com.example.backend.domain.groupmember.repository.GroupMemberRepository;
import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.repository.MemberRepository;
import com.example.backend.global.auth.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final CategoryRepository categoryRepository;
    private final CustomUserDetails customUserDetails;

    @Transactional
    public GroupResponseDto create(GroupRequestDto groupRequestDto){
        Member member = memberRepository.findById(groupRequestDto.getMemberId()).orElseThrow(()->new GroupException(GroupErrorCode.NOT_FOUND_MEMBER));

        List<Category> categories = categoryRepository.findAllById(groupRequestDto.getCategoryIds());
        Group group = Group.builder()
                .title(groupRequestDto.getTitle())
                .description(groupRequestDto.getDescription())
                .member(member)
                .status(GroupStatus.RECRUITING)
                .maxParticipants(groupRequestDto.getMaxParticipants())
                .build();

        List<GroupCategory> groupCategories = categories.stream()
                        .map(category -> new GroupCategory(group,category))
                                .collect(Collectors.toList());

        group.addGroupCategories(groupCategories);
        groupRepository.save(group);
        GroupMember groupMember = GroupMember.builder()
                .member(member)
                .group(group)
                .build();
        groupMemberRepository.save(groupMember);

        return new GroupResponseDto(group);
    }

    @Transactional(readOnly = true)
    public List<GroupResponseDto> findAllGroups() {
        List<GroupResponseDto> groups = groupRepository.findAll().stream().map(GroupResponseDto::new).collect(Collectors.toList());
        if (groups.isEmpty()) {
            throw new GroupException(GroupErrorCode.NOT_FOUND_LIST);
        }
        return groups;
    }

    @Transactional(readOnly = true)
    public GroupResponseDto findGroup(Long id){
        return groupRepository.findById(id).map(GroupResponseDto::new).orElseThrow(()->new GroupException(GroupErrorCode.NOT_FOUND));
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
        } else if (groupStatus == GroupStatus.NOT_RECRUITING){
            throw new GroupException(GroupErrorCode.NOT_RECRUITING);
        } else if (groupStatus == GroupStatus.COMPLETED) {
            throw new GroupException(GroupErrorCode.COMPLETED);
        } else if (groupStatus == GroupStatus.VOTING) {
            throw new GroupException(GroupErrorCode.VOTING);
        }
    }

    @Transactional
    public void joinGroup(Long groupId, Long memberId) {
        Group group = groupRepository.findById(groupId).orElseThrow(()-> new GroupException(GroupErrorCode.NOT_FOUND));

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GroupException(GroupErrorCode.NOT_FOUND_MEMBER));

        if (groupMemberRepository.existsByGroupAndMember(group, member)) {
            throw new GroupException(GroupErrorCode.EXISTED_MEMBER);
        }

        long currentMember = groupMemberRepository.countByGroup(group);
        if (currentMember > group.getMaxParticipants()-1) {
            throw new GroupException(GroupErrorCode.OVER_MEMBER);
        }

        GroupMember groupMember = GroupMember.builder()
                .group(group)
                .member(member)
                .build();

        groupMemberRepository.save(groupMember);
    }

    @Transactional(readOnly = true)
    public List<GroupResponseDto> getGroupByMemberId(){
        Long memberId = customUserDetails.getUserId();
        List<Group> groups = groupRepository.findGroupByMemberId(memberId);
        return groups.stream().map(GroupResponseDto::new).collect(Collectors.toList());
    }
}

