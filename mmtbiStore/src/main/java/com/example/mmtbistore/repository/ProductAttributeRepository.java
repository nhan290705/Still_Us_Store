package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.catalog.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {
    Optional<ProductAttribute> findByCode(String code);   // "size", "color"
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);
}
