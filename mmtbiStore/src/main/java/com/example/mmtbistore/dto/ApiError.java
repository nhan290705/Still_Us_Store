package com.example.mmtbistore.dto;

import org.springframework.http.HttpStatus;

import java.time.Instant;

/**
 * Khuôn JSON chung cho MỌI lỗi trả về giao diện.
 * VD: { "status": 404, "error": "Not Found", "message": "Không tìm thấy sản phẩm: abc",
 *       "path": "/api/products/abc", "timestamp": "2026-10-11T16:40:00Z" }
 */
public record ApiError(
        int status,
        String error,
        String message,
        String path,
        Instant timestamp
) {
    public static ApiError of(HttpStatus status, String message, String path) {
        return new ApiError(status.value(), status.getReasonPhrase(), message, path, Instant.now());
    }
}
