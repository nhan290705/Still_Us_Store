package com.example.mmtbistore.model.enums;

// 5 bước theo ghi chú của khách: Đã ghi nhận -> Đang chuẩn bị -> Chưa giao -> Đã bàn giao ĐVVC -> Đã giao
public enum OrderStatus { RECEIVED, PREPARING, WAITING_SHIPMENT, HANDED_TO_CARRIER, DELIVERED, CANCELLED, RETURNED }
