package com.example.mmtbistore.exception;

/**
 * Ném ra khi yêu cầu vi phạm nghiệp vụ (VD: tạo đợt COLLECTING khi đã có đợt khác đang COLLECTING).
 * GlobalExceptionHandler sẽ đổi lỗi này thành HTTP 400 kèm thông báo.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
