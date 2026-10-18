package com.example.mmtbistore.dto;

import com.example.mmtbistore.model.enums.OrderStatus;
import com.example.mmtbistore.model.enums.PaymentMethod;
import com.example.mmtbistore.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Kết quả tra cứu đơn hàng (khách nhập SĐT + mã đơn).
 * Chỉ trả những gì khách cần xem, không trả id nội bộ của database.
 */
public record OrderLookupResponse(
        String orderCode,
        OrderStatus status,
        String statusLabel,
        Instant createdAt,
        String receiverName,
        String shippingAddress,
        List<Item> items,
        BigDecimal subtotal,
        BigDecimal discountTotal,
        BigDecimal shippingFee,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        String shippingCarrier,
        String trackingNumber,
        List<HistoryEntry> history
) {

    // Mỗi đôi dép trong đơn (mỗi đôi là 1 dòng order_items riêng)
    public record Item(
            String productName,
            String productImageUrl,
            String variantLabel,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal lineTotal,
            String engravingText
    ) {
    }

    // Mỗi lần đơn đổi trạng thái (để vẽ thanh tiến trình 5 bước)
    public record HistoryEntry(
            OrderStatus status,
            String statusLabel,
            Instant changedAt,
            String note
    ) {
    }
}
