package com.example.mmtbistore.mapper;

import com.example.mmtbistore.dto.CategoryResponse;
import com.example.mmtbistore.model.catalog.Category;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        List<CategoryResponse> children = category.getChildren().stream()
                .filter(Category::isActive)
                .sorted(Comparator.comparingInt(Category::getSortOrder))
                .map(this::toResponse)
                .toList();

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getImageUrl(),
                children
        );
    }
}