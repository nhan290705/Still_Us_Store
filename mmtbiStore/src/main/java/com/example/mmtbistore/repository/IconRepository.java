package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.customization.Icon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IconRepository extends JpaRepository<Icon, Long> {
    List<Icon> findByIsActiveTrueOrderByGroupNameAsc();   // thư viện icon cho trang custom
}
