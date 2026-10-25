package com.example.mmtbistore.service;

import com.example.mmtbistore.dto.AdminMeResponse;
import com.example.mmtbistore.dto.LoginRequest;
import com.example.mmtbistore.dto.LoginResponse;
import com.example.mmtbistore.exception.ForbiddenException;
import com.example.mmtbistore.exception.ResourceNotFoundException;
import com.example.mmtbistore.exception.UnauthorizedException;
import com.example.mmtbistore.model.user.AppUser;
import com.example.mmtbistore.repository.AppUserRepository;
import com.example.mmtbistore.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        AppUser user = appUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Sai tên đăng nhập hoặc mật khẩu"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Sai tên đăng nhập hoặc mật khẩu");
        }

        if (!user.isActive()) {
            throw new ForbiddenException("Tài khoản đã dừng hợp tác");
        }

        user.setLastLoginAt(Instant.now());
        appUserRepository.save(user);

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name(), user.getFullName());

        return LoginResponse.builder()
                .token(token)
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }

    @Transactional(readOnly = true)
    public AdminMeResponse getMe(String username) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng: " + username));

        return AdminMeResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .isActive(user.isActive())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
}
