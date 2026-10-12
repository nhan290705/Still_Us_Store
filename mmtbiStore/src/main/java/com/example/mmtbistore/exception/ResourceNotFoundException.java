package com.example.mmtbistore.exception;

/**
 * Ném ra khi người dùng hỏi đích danh một thứ không tồn tại (sản phẩm, đơn hàng...).
 * GlobalExceptionHandler sẽ đổi lỗi này thành HTTP 404 kèm thông báo.
 *
 * VD: throw new ResourceNotFoundException("Không tìm thấy sản phẩm: abc");
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
