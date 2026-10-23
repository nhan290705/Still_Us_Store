package com.example.mmtbistore.dto;

import com.example.mmtbistore.model.enums.BatchStatus;

import java.time.Instant;

/**
 * Response trả về cho client khi đọc thông tin đợt bán.
 */
public record SaleBatchResponse(
        Long id,
        String name,
        BatchStatus status,
        Instant openedAt,
        Instant closedAt,
        Instant completedAt,
        String note,
        Instant createdAt,
        Instant updatedAt
) {}
