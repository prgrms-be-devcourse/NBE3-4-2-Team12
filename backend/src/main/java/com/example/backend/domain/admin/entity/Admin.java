package com.example.backend.domain.admin.entity;

import com.example.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column
    private String refreshToken;

    @Column
    private LocalDateTime expiryDate;

    public enum  Role {
        ADMIN  // 관리자 권한
    }

    @Builder
    public Admin(String adminName, String password, String refreshToken, LocalDateTime expiryDate) {
        this.adminName = adminName;
        this.password = password;
        this.refreshToken = refreshToken;
        this.expiryDate = expiryDate;
    }
}