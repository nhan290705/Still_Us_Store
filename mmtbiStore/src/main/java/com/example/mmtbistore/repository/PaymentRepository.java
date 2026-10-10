package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.order.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByMomoOrderId(String momoOrderId);   // dùng khi nhận IPN
}
