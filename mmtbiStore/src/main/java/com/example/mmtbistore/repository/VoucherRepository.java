package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.enums.VoucherStatus;
import com.example.mmtbistore.model.memory.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    // Voucher còn dùng được của một chuỗi STILL US (để thu hồi khi khách huỷ chuỗi)
    List<Voucher> findByMemoryPage_IdAndStatus(Long memoryPageId, VoucherStatus status);
}
