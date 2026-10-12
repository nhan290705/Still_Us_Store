package com.example.mmtbistore.dto;

import java.util.List;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String imageUrl,
        List<CategoryResponse> children
    ) {
}
