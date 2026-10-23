package com.example.mmtbistore.exception;

/**
 * Ném ra khi người dùng không có quyền làm việc này (VD: huỷ chuỗi STILL US mà SĐT không khớp).
 * GlobalExceptionHandler sẽ đổi lỗi này thành HTTP 403 kèm thông báo.
 *
 * VD: throw new ForbiddenException("Số điện thoại không khớp với chuỗi STILL US này");
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
