package com.example.backend.domain.groupmember.repository;

import com.example.backend.domain.group.entity.Group;
import com.example.backend.domain.groupmember.entity.GroupMember;
import com.example.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    boolean existsByGroupAndMember(Group group, Member member);
    long countByGroup(Group group);
    List<GroupMember> findByMember(Member member);
}
