package com.example.mmtbistore.service;

import com.example.mmtbistore.dto.CreateStaffRequest;
import com.example.mmtbistore.dto.PageResponse;
import com.example.mmtbistore.dto.StaffResponse;
import com.example.mmtbistore.dto.UpdateStaffRequest;
import com.example.mmtbistore.dto.UpdateStaffStatusRequest;
import com.example.mmtbistore.exception.BadRequestException;
import com.example.mmtbistore.exception.ConflictException;
import com.example.mmtbistore.exception.ResourceNotFoundException;
import com.example.mmtbistore.model.user.AppUser;
import com.example.mmtbistore.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminStaffService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public PageResponse<StaffResponse> getStaffList(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AppUser> userPage;
        if (search != null && !search.trim().isEmpty()) {
            userPage = appUserRepository.searchStaff(search.trim(), pageable);
        } else {
            userPage = appUserRepository.findAll(pageable);
        }
        return PageResponse.from(userPage.map(this::toResponse));
    }

    @Transactional
    public StaffResponse createStaff(CreateStaffRequest request) {
        String username = request.getUsername().trim();
        if (appUserRepository.existsByUsername(username)) {
            throw new ConflictException("Tên đăng nhập '" + username + "' đã tồn tại");
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName().trim());
        user.setEmail(request.getEmail() != null ? request.getEmail().trim() : null);
        user.setPhoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber().trim() : null);
        user.setRole(request.getRole());
        user.setActive(true);

        AppUser savedUser = appUserRepository.save(user);
        return toResponse(savedUser);
    }

    @Transactional
    public StaffResponse updateStaff(Long id, UpdateStaffRequest request) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id));

        user.setFullName(request.getFullName().trim());
        user.setEmail(request.getEmail() != null ? request.getEmail().trim() : null);
        user.setPhoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber().trim() : null);
        user.setRole(request.getRole());

        // Đổi mật khẩu là tùy chọn: chỉ đổi khi có truyền và không rỗng
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword().trim()));
        }

        AppUser updatedUser = appUserRepository.save(user);
        return toResponse(updatedUser);
    }

    @Transactional
    public StaffResponse updateStatus(Long id, UpdateStaffStatusRequest request, String currentAdminUsername) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id));

        // Không cho admin tự khoá tài khoản của mình
        if (user.getUsername().equalsIgnoreCase(currentAdminUsername) && Boolean.FALSE.equals(request.getIsActive())) {
            throw new BadRequestException("Admin không thể tự khoá tài khoản của chính mình");
        }

        user.setActive(request.getIsActive());
        AppUser savedUser = appUserRepository.save(user);
        return toResponse(savedUser);
    }

    private StaffResponse toResponse(AppUser user) {
        return StaffResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .isActive(user.isActive())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
