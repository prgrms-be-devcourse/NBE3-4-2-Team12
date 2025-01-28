package com.example.backend.domain.group.entity;

import com.example.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql="UPDATE group SET is_deleted = true WHERE id = ?")
@Table(name = "\"groups\"")
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목은 필수 사항입니다.")
    private String title;

    @NotBlank(message = "설명은 필수 사항입니다.")
    private String description;

    private Long ownerId;

    private GroupStatus status;

    private Integer maxParticipants;

    private boolean isDeleted = false;
}
