package com.example.mmtbistore.exception;

public class ConflictException extends RuntimeException{
    public ConflictException(String message) {
        super(message);
    }
}
// 409 yeu cau dung nhung da trung voi du lieu da co vd: dang ky lan thu hai.