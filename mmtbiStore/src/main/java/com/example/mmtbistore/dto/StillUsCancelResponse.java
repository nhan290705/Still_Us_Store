package com.example.mmtbistore.dto;

import com.example.mmtbistore.model.enums.StillUsStatus;

import java.time.Instant;

/**
 * Kết quả huỷ chuỗi STILL US. revokedVouchers = số voucher còn dùng được đã bị thu hồi.
 */
public record StillUsCancelResponse(
        String code,
        StillUsStatus status,
        Instant cancelledAt,
        int revokedVouchers
) {
}
