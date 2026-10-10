package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.order.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {
    List<OrderStatusHistory> findByOrder_IdOrderByCreatedAtAsc(Long orderId);   // timeline vận chuyển
}
