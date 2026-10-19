package com.example.mmtbistore.exception;

import com.example.mmtbistore.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Bắt lỗi từ MỌI controller ở một chỗ, đổi thành JSON ApiError với mã HTTP đúng.
 * Controller/Service chỉ cần "throw", không phải tự try-catch.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404: hỏi một thứ không tồn tại (VD: /api/products/khong-co)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest request){
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleInvalidbody(MethodArgumentNotValidException ex, HttpServletRequest request ){
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Dữ liệu gửi lên không hợp lệ");
        return build(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableBody(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Dữ liệu gửi lên không đúng định dạng JSON", request);
    }

    // 409: xung đột với dữ liệu đang có (VD: đơn đã đăng ký STILL US)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException ex, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request);
    }
    // 404: gõ sai đường dẫn (VD: /api/product)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoPath(NoResourceFoundException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, "Không tồn tại đường dẫn: " + request.getRequestURI(), request);
    }

    // 400: tham số sai kiểu (VD: ?page=abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = "Tham số '" + ex.getName() + "' không hợp lệ: " + ex.getValue();
        return build(HttpStatus.BAD_REQUEST, message, request);
    }

    // 405: sai phương thức (VD: POST vào API chỉ nhận GET)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, "Không hỗ trợ phương thức " + ex.getMethod(), request);
    }

    // Mọi lỗi còn lại
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleOther(Exception ex, HttpServletRequest request) {
        // Các lỗi chuẩn khác của Spring (thiếu tham số, sai định dạng body...) đã tự mang mã 4xx -> giữ nguyên mã đó
        if (ex instanceof ErrorResponse springError) {
            HttpStatus status = HttpStatus.valueOf(springError.getStatusCode().value());
            return build(status, ex.getMessage(), request);
        }

        // Lỗi thật sự của server: ghi đầy đủ vào log để sửa, nhưng KHÔNG lộ chi tiết ra ngoài
        log.error("Lỗi không mong muốn tại {}", request.getRequestURI(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Có lỗi xảy ra, vui lòng thử lại sau", request);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(ApiError.of(status, message, request.getRequestURI()));
    }
}
