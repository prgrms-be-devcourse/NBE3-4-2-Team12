package com.example.backend.domain.admin.service;

import com.example.backend.domain.admin.entity.Admin;
import com.example.backend.domain.admin.exception.AdminErrorCode;
import com.example.backend.domain.admin.exception.AdminException;
import com.example.backend.domain.admin.repository.AdminRepository;
import com.example.backend.global.auth.service.CookieService;
import com.example.backend.global.auth.util.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final CookieService cookieService;
    private final TokenProvider jwtProvider;

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

    // 엑세스 토큰 생성
    public String generateToken(Admin admin) {
        return this.jwtProvider.generateToken(admin);
    }

    // 리프레시 토큰 저장 및 반환
    public String generateAndSaveRefreshToken(Admin admin) {
        String refreshToken = this.jwtProvider.generateRefreshToken();
        LocalDateTime expiryDate = this.jwtProvider.getRefreshTokenExpiryDate();

        admin.setRefreshToken(refreshToken);
        admin.setRefreshTokenExpiryDate(expiryDate);
        this.adminRepository.save(admin);

        return refreshToken;
    }

    // admin 객체에 리프레시 토큰 만료
    public void clearRefreshToken(HttpServletRequest request) {
        String refreshToken = cookieService.getRefreshTokenFromCookie(request);

        if(refreshToken != null) {
            Admin admin = this.adminRepository.findByRefreshToken(refreshToken);
            if(admin != null) {
                admin.setRefreshToken(null);
                admin.setRefreshTokenExpiryDate(null);
                this.adminRepository.save(admin);
            }
        }

    }
}
