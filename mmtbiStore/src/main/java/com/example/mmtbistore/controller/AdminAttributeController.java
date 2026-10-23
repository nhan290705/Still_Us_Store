package com.example.mmtbistore.controller;

import com.example.mmtbistore.dto.AttributeRequest;
import com.example.mmtbistore.dto.AttributeResponse;
import com.example.mmtbistore.dto.AttributeValueRequest;
import com.example.mmtbistore.dto.AttributeValueResponse;
import com.example.mmtbistore.service.AdminAttributeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/attributes")
@RequiredArgsConstructor
@Tag(name = "Admin Attributes", description = "API quản lý thuộc tính và giá trị biến thể (Admin)")
public class AdminAttributeController {

    private final AdminAttributeService adminAttributeService;

    @Operation(summary = "Xem danh sách thuộc tính", description = "Lấy tất cả thuộc tính kèm danh sách giá trị")
    @GetMapping
    public ResponseEntity<List<AttributeResponse>> getAllAttributes() {
        return ResponseEntity.ok(adminAttributeService.getAllAttributes());
    }

    @Operation(summary = "Chi tiết thuộc tính", description = "Lấy thông tin thuộc tính theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<AttributeResponse> getAttributeById(@PathVariable Long id) {
        return ResponseEntity.ok(adminAttributeService.getAttributeById(id));
    }

    @Operation(summary = "Thêm thuộc tính mới", description = "Tạo thuộc tính mới như Màu sắc, Kích cỡ (Kiểm tra trùng code)")
    @PostMapping
    public ResponseEntity<AttributeResponse> createAttribute(@Valid @RequestBody AttributeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminAttributeService.createAttribute(request));
    }

    @Operation(summary = "Sửa thuộc tính", description = "Cập nhật tên, mã code, kiểu hiển thị của thuộc tính")
    @PutMapping("/{id}")
    public ResponseEntity<AttributeResponse> updateAttribute(
            @PathVariable Long id,
            @Valid @RequestBody AttributeRequest request
    ) {
        return ResponseEntity.ok(adminAttributeService.updateAttribute(id, request));
    }

    @Operation(summary = "Thêm giá trị cho thuộc tính", description = "Thêm một giá trị mới (ví dụ Size 41, Màu Xanh)")
    @PostMapping("/{attributeId}/values")
    public ResponseEntity<AttributeValueResponse> addAttributeValue(
            @PathVariable Long attributeId,
            @Valid @RequestBody AttributeValueRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminAttributeService.addAttributeValue(attributeId, request));
    }

    @Operation(summary = "Sửa giá trị thuộc tính", description = "Cập nhật giá trị, mã màu hex, thứ tự hiển thị")
    @PutMapping("/values/{valueId}")
    public ResponseEntity<AttributeValueResponse> updateAttributeValue(
            @PathVariable Long valueId,
            @Valid @RequestBody AttributeValueRequest request
    ) {
        return ResponseEntity.ok(adminAttributeService.updateAttributeValue(valueId, request));
    }

    @Operation(summary = "Xoá giá trị thuộc tính", description = "Xoá một giá trị thuộc tính")
    @DeleteMapping("/values/{valueId}")
    public ResponseEntity<Void> deleteAttributeValue(@PathVariable Long valueId) {
        adminAttributeService.deleteAttributeValue(valueId);
        return ResponseEntity.noContent().build();
    }
}
