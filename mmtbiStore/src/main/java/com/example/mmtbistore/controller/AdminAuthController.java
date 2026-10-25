package com.example.mmtbistore.controller;

import com.example.mmtbistore.dto.AdminMeResponse;
import com.example.mmtbistore.dto.LoginRequest;
import com.example.mmtbistore.dto.LoginResponse;
import com.example.mmtbistore.exception.UnauthorizedException;
import com.example.mmtbistore.service.AdminAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
@Tag(name = "Admin Auth", description = "API xác thực tài khoản quản trị Dashboard")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @Operation(summary = "Đăng nhập Dashboard", description = "Đăng nhập bằng username và password, nhận JWT token")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(adminAuthService.login(request));
    }

    @Operation(summary = "Lấy thông tin người dùng đang đăng nhập", description = "Lấy thông tin tài khoản qua JWT token")
    @GetMapping("/me")
    public ResponseEntity<AdminMeResponse> getMe(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new UnauthorizedException("Yêu cầu đăng nhập");
        }
        return ResponseEntity.ok(adminAuthService.getMe(authentication.getName()));
    }
}
