package com.example.backend.domain.group.entity;

import com.example.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@Table(name = "\"groups\"")
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Long ownerId;

    private GroupStatus status;

    private Integer maxParticipants;

    public void update(String title, String description, Integer maxParticipants) {
        this.title = title;
        this.description = description;
        this.maxParticipants = maxParticipants;
    }

    public void updateStatus(GroupStatus status) {
        this.status = status;
    }
}
