package com.example.backend.domain.admin.repository;

import com.example.backend.domain.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByAdminName(String adminName);
    Optional<Admin> findByRefreshToken(String refreshToken);
}
