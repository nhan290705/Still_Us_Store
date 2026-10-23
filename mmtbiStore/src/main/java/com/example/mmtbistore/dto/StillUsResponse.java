package com.example.mmtbistore.dto;

import com.example.mmtbistore.model.enums.StillUsStatus;

import java.time.Instant;

/**
 * Kết quả đăng ký STILL US. code = memory_pages.slug, dùng làm đường dẫn của mã QR.
 */
public record StillUsResponse(
        String code,
        String ownerName,
        String partnerName,
        StillUsStatus status,
        Instant startedAt,
        Instant endsAt
) {
}
