package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.catalog.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProduct_IdOrderBySortOrderAsc(Long productId);
}
