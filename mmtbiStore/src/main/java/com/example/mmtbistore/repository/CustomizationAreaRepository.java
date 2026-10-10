package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.customization.CustomizationArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomizationAreaRepository extends JpaRepository<CustomizationArea, Long> {
    List<CustomizationArea> findByProduct_IdOrderBySortOrderAsc(Long productId);
}
