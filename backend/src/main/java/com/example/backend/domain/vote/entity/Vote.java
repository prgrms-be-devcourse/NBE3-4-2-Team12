package com.example.backend.domain.vote.entity;

import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.voter.entity.Voter;
import com.example.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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

}