package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.customization.DesignTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DesignTemplateRepository extends JpaRepository<DesignTemplate, Long> {
    // Template riêng của sản phẩm + template dùng chung (product = null)
    @Query("""
           select t from DesignTemplate t
           where t.isActive = true
             and (t.product.id = :productId or t.product is null)
           """)
    List<DesignTemplate> findAvailableForProduct(@Param("productId") Long productId);
}
