package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.memory.MemoryPageMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoryPageMediaRepository extends JpaRepository<MemoryPageMedia, Long> {
    List<MemoryPageMedia> findByMemoryPage_IdOrderBySortOrderAsc(Long memoryPageId);
}
