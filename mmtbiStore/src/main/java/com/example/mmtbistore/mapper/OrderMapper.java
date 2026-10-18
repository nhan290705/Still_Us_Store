package com.example.mmtbistore.mapper;

import com.example.mmtbistore.dto.OrderLookupResponse;
import com.example.mmtbistore.model.order.Order;
import com.example.mmtbistore.model.order.OrderItem;
import com.example.mmtbistore.model.order.OrderStatusHistory;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class OrderMapper {

    public OrderLookupResponse toLookup(Order order) {
        List<OrderLookupResponse.Item> items = order.getItems().stream()
                .sorted(Comparator.comparing(OrderItem::getId))
                .map(this::toItem)
                .toList();

        // statusHistories đã được sắp theo thời gian (@OrderBy trong entity Order)
        List<OrderLookupResponse.HistoryEntry> history = order.getStatusHistories().stream()
                .map(this::toHistory)
                .toList();

        return new OrderLookupResponse(
                order.getOrderCode(),
                order.getStatus(),
                order.getStatus().getLabel(),
                order.getCreatedAt(),
                order.getReceiverName(),
                shippingAddress(order),
                items,
                order.getSubtotal(),
                order.getDiscountTotal(),
                order.getShippingFee(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getPaymentStatus(),
                order.getShippingCarrier(),
                order.getTrackingNumber(),
                history
        );
    }

    private OrderLookupResponse.Item toItem(OrderItem item) {
        return new OrderLookupResponse.Item(
                item.getProductName(),
                item.getProductImageUrl(),
                item.getVariantLabel(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getLineTotal(),
                item.getEngravingText()
        );
    }

    private OrderLookupResponse.HistoryEntry toHistory(OrderStatusHistory history) {
        return new OrderLookupResponse.HistoryEntry(
                history.getToStatus(),
                history.getToStatus().getLabel(),
                history.getCreatedAt(),
                history.getNote()
        );
    }

    // VD: "12 Lý Thường Kiệt, Phường Cửa Nam, Hà Nội"
    private String shippingAddress(Order order) {
        return String.join(", ", order.getAddressDetail(), order.getWard(), order.getProvince());
    }
}