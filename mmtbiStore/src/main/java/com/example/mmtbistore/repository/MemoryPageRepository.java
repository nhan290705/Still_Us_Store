package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.memory.MemoryPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemoryPageRepository extends JpaRepository<MemoryPage, Long> {
    Optional<MemoryPage> findBySlug(String slug);              // quét QR
    Optional<MemoryPage> findByOrder_Id(Long orderId);
}
