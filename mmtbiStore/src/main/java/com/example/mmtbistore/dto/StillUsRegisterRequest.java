package com.example.mmtbistore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Body của POST /api/still-us: khách gửi mã đơn + SĐT đặt hàng + tên hai người.
 * Các chú thích @NotBlank, @Size được kiểm tra khi controller có @Valid.
 */
public record StillUsRegisterRequest(
        @NotBlank(message = "Vui lòng nhập mã đơn hàng")
        String orderCode,

        @NotBlank(message = "Vui lòng nhập số điện thoại")
        String phone,

        @NotBlank(message = "Vui lòng nhập tên của bạn")
        @Size(max = 100, message = "Tên tối đa 100 ký tự")
        String ownerName,

        @NotBlank(message = "Vui lòng nhập tên người ấy")
        @Size(max = 100, message = "Tên tối đa 100 ký tự")
        String partnerName
) {
}
