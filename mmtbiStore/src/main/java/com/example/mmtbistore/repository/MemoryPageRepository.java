package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.enums.StillUsStatus;
import com.example.mmtbistore.model.memory.MemoryPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface MemoryPageRepository extends JpaRepository<MemoryPage, Long> {
    Optional<MemoryPage> findBySlug(String slug);              // quét QR
    Optional<MemoryPage> findByOrder_Id(Long orderId);

    boolean existsBySlug(String slug);
    boolean existsByOrder_Customer_IdAndStatusAndStartedAtIsNotNull(Long customerId, StillUsStatus status);

    // Chuỗi thứ mấy của khách: đếm các chuỗi đã đăng ký từ trước tới chuỗi này
    long countByOrder_Customer_IdAndStartedAtLessThanEqual(Long customerId, Instant startedAt);
}
