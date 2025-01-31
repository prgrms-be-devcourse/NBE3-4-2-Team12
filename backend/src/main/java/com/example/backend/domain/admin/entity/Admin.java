package com.example.backend.domain.admin.entity;

import com.example.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String admin_name; //  관리자 아이디

    @Column
    private String password; //  비밀번호 (암호화 저장)

    @Enumerated(EnumType.STRING)
    private Role role;  // 관리자 권한 (ADMIN)

    @Builder
    public Admin(String admin_name, String password, Role role) {
        this.admin_name = admin_name;
        this.password = password;
        this.role = role;
    }

    public enum  Role {
        ADMIN  // 관리자 권한
    }
}