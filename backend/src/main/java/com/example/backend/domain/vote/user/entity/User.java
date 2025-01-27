package com.example.backend.domain.vote.user.entity;

import com.example.backend.domain.vote.entity.Vote;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User가 생성한 Vote 목록 (일대다)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Vote> votes = new ArrayList<>();
}
