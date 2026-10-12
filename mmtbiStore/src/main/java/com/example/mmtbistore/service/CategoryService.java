package com.example.mmtbistore.service;

import com.example.mmtbistore.dto.CategoryResponse;
import com.example.mmtbistore.mapper.CategoryMapper;
import com.example.mmtbistore.model.catalog.Category;
import com.example.mmtbistore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getActiveCategories() {
        return categoryRepository.findByParentIsNullOrderBySortOrderAsc().stream()
                .filter(Category::isActive)
                .map(categoryMapper::toResponse)
                .toList();
    }
}