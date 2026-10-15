package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.memory.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
}
