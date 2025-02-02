package com.example.backend.domain.group.entity;

import com.example.backend.domain.member.entity.Member;
import com.example.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "\"groups\"")
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column
    private GroupStatus status;

    @Column
    private Integer maxParticipants;


    @Builder
    public Group(String title, String description, Member member, GroupStatus status, Integer maxParticipants) {
        this.title = title;
        this.description = description;
        this.member = member;
        this.status = status;
        this.maxParticipants = maxParticipants;
    }

    public void update(String title, String description, Integer maxParticipants) {
        this.title = title;
        this.description = description;
        this.maxParticipants = maxParticipants;
    }

    public void updateStatus(GroupStatus status) {
        this.status = status;
    }
}
