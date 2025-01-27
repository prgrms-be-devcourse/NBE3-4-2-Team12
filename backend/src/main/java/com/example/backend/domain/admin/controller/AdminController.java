package com.example.backend.domain.admin.controller;

import com.example.backend.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminService adminService;
}
