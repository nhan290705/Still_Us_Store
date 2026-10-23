package com.example.mmtbistore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Body cho POST / PUT /api/admin/batches.
 */
public record SaleBatchRequest(

        @NotBlank(message = "Tên đợt không được để trống")
        @Size(max = 100, message = "Tên đợt tối đa 100 ký tự")
        String name,

        @Size(max = 500, message = "Ghi chú tối đa 500 ký tự")
        String note
) {}
