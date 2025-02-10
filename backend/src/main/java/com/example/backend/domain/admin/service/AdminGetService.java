package com.example.backend.domain.admin.service;

import com.example.backend.domain.admin.entity.Admin;
import com.example.backend.domain.admin.exception.AdminErrorCode;
import com.example.backend.domain.admin.exception.AdminException;
import com.example.backend.domain.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminGetService {

    private final AdminRepository adminRepository;

    // 관리자 name 으로 가져오기
    @Transactional
    public Admin getAdminByName(String name) {
        return this.adminRepository.findByAdminName(name)
                .orElseThrow(() -> new AdminException(AdminErrorCode.NOT_FOUND_ADMIN));
    }

    // 리프레시 토큰으로 가져오기
    @Transactional(readOnly = true)
    public Admin getAdminByRefreshToken(String refreshToken) {
        return this.adminRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AdminException(AdminErrorCode.NOT_FOUND_ADMIN));
    }

}
