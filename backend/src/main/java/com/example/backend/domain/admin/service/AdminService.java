package com.example.backend.domain.admin.service;

import com.example.backend.domain.admin.entity.Admin;
import com.example.backend.domain.admin.exception.AdminErrorCode;
import com.example.backend.domain.admin.exception.AdminException;
import com.example.backend.domain.admin.repository.AdminRepository;
import com.example.backend.global.auth.service.CookieService;
import com.example.backend.global.auth.util.TokenProvider;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class AdminService {

    private  final AdminGetService adminGetService;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final CookieService cookieService;
    private final TokenProvider jwtProvider;

    @Transactional(readOnly = true)
    public Admin getAdmin(String adminName, String password) {
        // 아이디로 관리자 조회
        Admin admin = this.adminGetService.getAdminByName(adminName);

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new AdminException(AdminErrorCode.INVALID_CREDENTIALS);
        }

        return admin;
    }

    // 엑세스 토큰 쿠키 저장
    @Transactional
    public void generateToken(Admin admin, HttpServletResponse response) {
        String accessToken = this.jwtProvider.generateToken(admin);
        this.cookieService.addAccessTokenToCookie(accessToken, response);
    }

    // 리프레시 토큰 저장
    @Transactional
    public void generateAndSaveRefreshToken(Admin admin, HttpServletResponse response) {
        String refreshToken = this.jwtProvider.generateRefreshToken();
        LocalDateTime expiryDate = this.jwtProvider.getRefreshTokenExpiryDate();

        admin.setRefreshToken(refreshToken);
        admin.setRefreshTokenExpiryDate(expiryDate);
        this.adminRepository.save(admin);
        this.cookieService.addRefreshTokenToCookie(refreshToken, response);
    }

    // admin 객체에 리프레시 토큰 만료
    @Transactional
    public void logout(HttpServletResponse response, HttpServletRequest request) {
        this.cookieService.clearTokenFromCookie(response);

        String refreshToken = cookieService.getRefreshTokenFromCookie(request);
        if(refreshToken != null) {
            Admin admin = this.adminGetService.getAdminByRefreshToken(refreshToken);
                admin.setRefreshToken(null);
                admin.setRefreshTokenExpiryDate(null);
                this.adminRepository.save(admin);
        }
    }
}
