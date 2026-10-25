package com.example.mmtbistore.controller;

import com.example.mmtbistore.dto.UploadResponse;
import com.example.mmtbistore.service.AdminUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/uploads")
@RequiredArgsConstructor
@Tag(name = "Admin Uploads", description = "API tải file ảnh sản phẩm lên máy chủ (Admin)")
public class AdminUploadController {

    private final AdminUploadService adminUploadService;

    @Operation(summary = "Tải ảnh lên máy chủ", description = "Nhận file ảnh (JPG, PNG, WEBP tối đa 5MB) và lưu vào thư mục tĩnh trên server")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminUploadService.uploadImage(file));
    }
}
