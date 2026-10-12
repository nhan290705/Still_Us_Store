package com.example.mmtbistore.dto;

import java.math.BigDecimal;

public record ProductCardResponse(
        Long id,
        String name,
        String slug,
        String shortDescription,
        BigDecimal price,
        String imageUrl,
        boolean inStock
) {
}