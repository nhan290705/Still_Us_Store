package com.example.mmtbistore.dto;

import com.example.mmtbistore.model.enums.StillUsStatus;

import java.time.Instant;
import java.util.List;

/**
 * Trang STILL US công khai (ai quét QR cũng xem được): KHÔNG có SĐT, KHÔNG có thông tin đơn.
 * registered = false: mã QR đã in sẵn nhưng khách chưa đăng ký -> giao diện hiện form đăng ký.
 * Giao diện tự đếm ngược giờ/phút/giây từ endsAt.
 */
public record StillUsDetailResponse(
        String code,
        boolean registered,
        String ownerName,
        String partnerName,
        StillUsStatus status,
        Instant startedAt,
        Instant endsAt,
        Instant cancelledAt,
        long daysLeft,
        long yearNumber,
        String themeKey,
        List<Milestone> milestones
) {

    // Mốc 90 / 180 / 365 ngày: ngày đến mốc và đã qua chưa
    public record Milestone(
            int day,
            Instant date,
            boolean reached
    ) {
    }
}
