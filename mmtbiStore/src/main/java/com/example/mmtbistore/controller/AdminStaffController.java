package com.example.mmtbistore.controller;

import com.example.mmtbistore.dto.CreateStaffRequest;
import com.example.mmtbistore.dto.PageResponse;
import com.example.mmtbistore.dto.StaffResponse;
import com.example.mmtbistore.dto.UpdateStaffRequest;
import com.example.mmtbistore.dto.UpdateStaffStatusRequest;
import com.example.mmtbistore.service.AdminStaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/staff")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Staff", description = "API quản lý nhân sự (Chỉ dành riêng cho ADMIN)")
public class AdminStaffController {

    private final AdminStaffService adminStaffService;

    @Operation(summary = "Danh sách nhân viên", description = "Lấy danh sách nhân sự phân trang, có thể tìm kiếm theo từ khoá")
    @GetMapping
    public ResponseEntity<PageResponse<StaffResponse>> getStaffList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(adminStaffService.getStaffList(page, size, search));
    }

    @Operation(summary = "Thêm nhân viên mới", description = "Tạo mới tài khoản nhân viên hoặc admin (Username không trùng)")
    @PostMapping
    public ResponseEntity<StaffResponse> createStaff(@Valid @RequestBody CreateStaffRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminStaffService.createStaff(request));
    }

    @Operation(summary = "Cập nhật thông tin nhân viên", description = "Sửa họ tên, email, SĐT, vai trò; đổi mật khẩu là tùy chọn")
    @PutMapping("/{id}")
    public ResponseEntity<StaffResponse> updateStaff(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStaffRequest request
    ) {
        return ResponseEntity.ok(adminStaffService.updateStaff(id, request));
    }

    @Operation(summary = "Đổi trạng thái nhân viên", description = "Bật/tắt trạng thái hoạt động. Không cho phép admin tự khóa chính mình")
    @PatchMapping("/{id}/status")
    public ResponseEntity<StaffResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStaffStatusRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(adminStaffService.updateStatus(id, request, authentication.getName()));
    }
}
