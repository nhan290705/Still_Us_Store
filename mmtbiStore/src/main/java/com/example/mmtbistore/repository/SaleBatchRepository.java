package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.enums.BatchStatus;
import com.example.mmtbistore.model.order.SaleBatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SaleBatchRepository extends JpaRepository<SaleBatch, Long> {

    /**
     * Tìm đợt đang ở trạng thái cho trước (dùng kiểm tra "đã có đợt COLLECTING chưa").
     */
    Optional<SaleBatch> findByStatus(BatchStatus status);

    /**
     * Kiểm tra nhanh xem đã tồn tại đợt ở trạng thái cho trước hay chưa.
     */
    boolean existsByStatus(BatchStatus status);
}
