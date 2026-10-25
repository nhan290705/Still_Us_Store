package com.example.mmtbistore.controller;

import com.example.mmtbistore.dto.AdminCategoryResponse;
import com.example.mmtbistore.dto.CategoryRequest;
import com.example.mmtbistore.service.AdminCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@Tag(name = "Admin Categories", description = "API quản lý danh mục sản phẩm (Admin)")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @Operation(summary = "Lấy toàn bộ danh mục", description = "Lấy tất cả danh mục kể cả danh mục đang bị ẩn")
    @GetMapping
    public ResponseEntity<List<AdminCategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(adminCategoryService.getAllCategories());
    }

    @Operation(summary = "Chi tiết danh mục", description = "Lấy thông tin chi tiết danh mục theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<AdminCategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(adminCategoryService.getCategoryById(id));
    }

    @Operation(summary = "Thêm danh mục mới", description = "Tạo danh mục mới (Kiểm tra trùng slug)")
    @PostMapping
    public ResponseEntity<AdminCategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminCategoryService.createCategory(request));
    }

    @Operation(summary = "Cập nhật danh mục", description = "Sửa thông tin tên, slug, ảnh, danh mục cha, ẩn/hiện")
    @PutMapping("/{id}")
    public ResponseEntity<AdminCategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request
    ) {
        return ResponseEntity.ok(adminCategoryService.updateCategory(id, request));
    }

    @Operation(summary = "Xoá danh mục", description = "Xoá cứng danh mục (Chỉ cho phép khi danh mục không có sản phẩm)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        adminCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
