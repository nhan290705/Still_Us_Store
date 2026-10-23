package com.example.mmtbistore.exception;

// file gui du lieu sai vd "sdt khong hop le"
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
