package com.example.backend.domain.admin.entity;

import com.example.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin_name")
    private String adminName; //  관리자 아이디

    @Column
    private String password; //  비밀번호 (암호화 저장)

    @Enumerated(EnumType.STRING)
    private Role role = Role.ADMIN;  // 관리자 권한 (ADMIN)


    public enum  Role {
        ADMIN  // 관리자 권한
    }
}