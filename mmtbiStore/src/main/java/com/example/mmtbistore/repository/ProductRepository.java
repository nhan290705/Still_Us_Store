package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.catalog.Product;
import com.example.mmtbistore.model.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByStatusAndIsDeletedFalse(ProductStatus status, Pageable pageable);

    Page<Product> findByStatusAndIsDeletedFalseAndCategory_Slug(ProductStatus status, String categorySlug, Pageable pageable);

    Optional<Product> findBySlugAndStatusAndIsDeletedFalse(String slug, ProductStatus status);

    boolean existsByCategoryId(Long categoryId);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    @Query("""
        SELECT p FROM Product p
        WHERE p.isDeleted = false
          AND (:status IS NULL OR p.status = :status)
          AND (:categoryId IS NULL OR p.category.id = :categoryId)
          AND (
            :search IS NULL OR :search = ''
            OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(p.code) LIKE LOWER(CONCAT('%', :search, '%'))
          )
    """)
    Page<Product> searchAdminProducts(
            @Param("search") String search,
            @Param("status") ProductStatus status,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );
}