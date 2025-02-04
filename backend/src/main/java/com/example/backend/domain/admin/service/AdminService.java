package com.example.backend.domain.admin.service;

import com.example.backend.domain.admin.entity.Admin;
import com.example.backend.domain.admin.exception.AdminErrorCode;
import com.example.backend.domain.admin.exception.AdminException;
import com.example.backend.domain.admin.repository.AdminRepository;
import com.example.backend.global.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public Admin getAdmin(String adminName, String password) {
        // 아이디로 관리자 조회
        Admin admin = adminRepository.findByAdminName(adminName)
                .orElseThrow(() -> new AdminException(AdminErrorCode.NOT_FOUND_ADMIN));

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new AdminException(AdminErrorCode.INVALID_CREDENTIALS);
        }

        return admin;
    }

}
