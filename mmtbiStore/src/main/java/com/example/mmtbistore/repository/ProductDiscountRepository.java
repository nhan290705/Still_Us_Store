package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.catalog.ProductDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Long> {
    @Query("""
           select d from ProductDiscount d
           where d.product.id = :productId and d.isActive = true
             and :now between d.startAt and d.endAt
           """)
    List<ProductDiscount> findActive(@Param("productId") Long productId, @Param("now") Instant now);
}
