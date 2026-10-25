package com.example.mmtbistore.controller;

import com.example.mmtbistore.dto.*;
import com.example.mmtbistore.model.enums.ProductStatus;
import com.example.mmtbistore.service.AdminProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@Tag(name = "Admin Products", description = "API quản lý sản phẩm và biến thể (Admin)")
public class AdminProductController {

    private final AdminProductService adminProductService;

    @Operation(summary = "Danh sách sản phẩm (phân trang, lọc)", description = "Lấy danh sách sản phẩm có phân trang, lọc theo từ khoá, trạng thái, danh mục")
    @GetMapping
    public ResponseEntity<PageResponse<AdminProductCardResponse>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) Long categoryId
    ) {
        return ResponseEntity.ok(adminProductService.getProducts(page, size, search, status, categoryId));
    }

    @Operation(summary = "Chi tiết sản phẩm", description = "Lấy chi tiết sản phẩm và danh sách biến thể theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<AdminProductDetailResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(adminProductService.getProductById(id));
    }

    @Operation(summary = "Tạo sản phẩm mới", description = "Tạo sản phẩm mới (mặc định DRAFT, kiểm tra trùng slug, code)")
    @PostMapping
    public ResponseEntity<AdminProductDetailResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminProductService.createProduct(request));
    }

    @Operation(summary = "Cập nhật sản phẩm", description = "Cập nhật thông tin sản phẩm theo ID")
    @PutMapping("/{id}")
    public ResponseEntity<AdminProductDetailResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request
    ) {
        return ResponseEntity.ok(adminProductService.updateProduct(id, request));
    }

    @Operation(summary = "Cập nhật trạng thái sản phẩm", description = "Đổi trạng thái sản phẩm: DRAFT, ACTIVE, HIDDEN")
    @PatchMapping("/{id}/status")
    public ResponseEntity<AdminProductDetailResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ProductStatusRequest request
    ) {
        return ResponseEntity.ok(adminProductService.updateStatus(id, request.getStatus()));
    }

    @Operation(summary = "Xoá sản phẩm (Xoá mềm)", description = "Đánh dấu sản phẩm đã xoá (is_deleted = true)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        adminProductService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Thêm biến thể cho sản phẩm", description = "Tạo biến thể mới gắn với thuộc tính màu/cỡ (kiểm tra SKU trùng)")
    @PostMapping("/{productId}/variants")
    public ResponseEntity<AdminProductVariantResponse> addVariant(
            @PathVariable Long productId,
            @Valid @RequestBody ProductVariantRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminProductService.addVariant(productId, request));
    }

    @Operation(summary = "Cập nhật biến thể", description = "Cập nhật SKU, giá bù, tồn kho, ảnh, thuộc tính của biến thể")
    @PutMapping({"/variants/{variantId}", "/{productId}/variants/{variantId}"})
    public ResponseEntity<AdminProductVariantResponse> updateVariant(
            @PathVariable Long variantId,
            @Valid @RequestBody ProductVariantRequest request
    ) {
        return ResponseEntity.ok(adminProductService.updateVariant(variantId, request));
    }

    @Operation(summary = "Xoá biến thể", description = "Xoá biến thể sản phẩm theo ID")
    @DeleteMapping({"/variants/{variantId}", "/{productId}/variants/{variantId}"})
    public ResponseEntity<Void> deleteVariant(@PathVariable Long variantId) {
        adminProductService.deleteVariant(variantId);
        return ResponseEntity.noContent().build();
    }
}
