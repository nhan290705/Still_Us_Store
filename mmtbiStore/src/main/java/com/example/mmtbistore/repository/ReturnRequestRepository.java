package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.aftersales.ReturnRequest;
import com.example.mmtbistore.model.enums.ReturnStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Long> {
    Optional<ReturnRequest> findByRequestCode(String requestCode);
    List<ReturnRequest> findByOrder_IdOrderByCreatedAtDesc(Long orderId);
    List<ReturnRequest> findByStatusOrderByCreatedAtAsc(ReturnStatus status);   // danh sách chờ nhân viên xử lý
}
