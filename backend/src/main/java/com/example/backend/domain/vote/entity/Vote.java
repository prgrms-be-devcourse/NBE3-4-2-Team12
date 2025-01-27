package com.example.backend.domain.vote.entity;

import com.example.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Vote extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id")
    private Long groupId;

    private String location;
    private String address;
    private Double latitude;
    private Double longitude;

    @Column(name = "voter_count")
    private Integer voterCount;

}
