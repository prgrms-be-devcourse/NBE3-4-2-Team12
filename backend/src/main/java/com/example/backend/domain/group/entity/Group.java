package com.example.backend.domain.group.entity;

import com.example.backend.domain.groupcategory.GroupCategory;
import com.example.backend.domain.member.entity.Member;
import com.example.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    @Enumerated(value = EnumType.STRING)
    private GroupStatus status;

    @Column
    private Integer maxParticipants;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupCategory> groupCategories = new ArrayList<>();


    @Builder
    public Group(String title, String description, Member member, GroupStatus status, Integer maxParticipants, List<GroupCategory> groupCategories) {
        this.title = title;
        this.description = description;
        this.member = member;
        this.status = status;
        this.maxParticipants = maxParticipants;
        this.groupCategories = groupCategories;
    }

    public void update(String title, String description, Integer maxParticipants) {
        this.title = title;
        this.description = description;
        this.maxParticipants = maxParticipants;
    }

    public void updateStatus(GroupStatus status) {
        this.status = status;
    }

    public void addGroupCategories(List<GroupCategory> groupCategories) {
        this.groupCategories = groupCategories;
    }
}
