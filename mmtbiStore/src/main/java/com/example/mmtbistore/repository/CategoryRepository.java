package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.catalog.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findBySlug(String slug);
    boolean existsBySlug(String slug);
    boolean existsBySlugAndIdNot(String slug, Long id);
    boolean existsByParentId(Long parentId);
    List<Category> findByParentIsNullOrderBySortOrderAsc();   // danh mục cấp 1 cho menu
}