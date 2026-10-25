package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.catalog.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProduct_IdOrderBySortOrderAsc(Long productId);
    Optional<ProductImage> findByIdAndProduct_Id(Long id, Long productId);
}
