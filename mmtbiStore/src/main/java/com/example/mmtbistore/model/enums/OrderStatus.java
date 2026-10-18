package com.example.mmtbistore.model.enums;

public enum OrderStatus {
    RECEIVED("Đã ghi nhận"),
    PREPARING("Đang chuẩn bị"),
    WAITING_SHIPMENT("Chưa giao"),
    HANDED_TO_CARRIER("Đã bàn giao ĐVVC"),
    DELIVERED("Đã giao"),
    CANCELLED("Đã huỷ"),
    RETURNED("Hoàn trả");

    private final String label;

    OrderStatus (String label){
        this.label = label;
    }

    public String getLabel(){
        return this.label;
    }
}

