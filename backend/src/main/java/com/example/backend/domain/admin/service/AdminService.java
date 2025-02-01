package com.example.backend.domain.admin.service;

import com.example.backend.domain.admin.entity.Admin;
import com.example.backend.domain.admin.repository.AdminRepository;
import com.example.backend.global.config.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public String login(String adminName, String password) {
        // 아이디로 관리자 조회
        Admin admin = adminRepository.findByAdminName(adminName)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 관리자입니다."));

        // 비밀번호 검증
        if(!passwordEncoder.matches(password, admin.getPassword())) {
            throw new BadCredentialsException("비밀번호가 틀렸습니다.");
        }

        // JWT  토큰 발급
        return jwtProvider.generateToken(admin.getAdminName(), admin.getRole().name());
    }
}
