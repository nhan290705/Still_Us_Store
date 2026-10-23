package com.example.mmtbistore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Body của POST /api/still-us/{code}/cancel. SĐT phải khớp SĐT người đặt đơn.
 */
public record StillUsCancelRequest(
        @NotBlank(message = "Vui lòng nhập số điện thoại")
        String phone,

        @Size(max = 500, message = "Lý do tối đa 500 ký tự")
        String reason
) {
}
