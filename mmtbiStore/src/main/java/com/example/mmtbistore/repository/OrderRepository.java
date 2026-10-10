package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.enums.OrderStatus;
import com.example.mmtbistore.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderCode(String orderCode);

    // Tra cứu đơn: SĐT + mã đơn
    Optional<Order> findByOrderCodeAndCustomer_PhoneNumber(String orderCode, String phoneNumber);

    // Job huỷ đơn chờ thanh toán quá hạn
    List<Order> findByStatusAndCreatedAtBefore(OrderStatus status, Instant time);
}