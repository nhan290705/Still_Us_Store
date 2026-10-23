package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.catalog.Product;
import com.example.mmtbistore.model.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByStatusAndIsDeletedFalse(ProductStatus status, Pageable pageable);

    Page<Product> findByStatusAndIsDeletedFalseAndCategory_Slug(ProductStatus status, String categorySlug, Pageable pageable);

    Optional<Product> findBySlugAndStatusAndIsDeletedFalse(String slug, ProductStatus status);

    boolean existsByCategoryId(Long categoryId);
}