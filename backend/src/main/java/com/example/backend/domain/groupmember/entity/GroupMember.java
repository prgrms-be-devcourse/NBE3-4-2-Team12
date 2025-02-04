package com.example.backend.domain.groupmember.entity;

import com.example.backend.domain.group.entity.Group;
import com.example.backend.domain.member.entity.Member;
import com.example.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class GroupMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;


    @ManyToOne
    @JoinColumn(name = "groups_id", nullable = false)
    private Group group;

    @Enumerated(value = EnumType.STRING)
    private GroupMemberStatus groupMemberStatus;

    @Builder
    public GroupMember(Member member, Group group, GroupMemberStatus groupMemberStatus) {
        this.member = member;
        this.group = group;
        this.groupMemberStatus = GroupMemberStatus.APPLYING;
    }
}
