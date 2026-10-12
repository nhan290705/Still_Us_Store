package com.example.mmtbistore.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductDetailResponse(
        Long id,
        String name,
        String slug,
        String shortDescription,
        String description,
        BigDecimal basePrice,
        String categoryName,
        String categorySlug,
        boolean inStock,
        List<String> images,
        List<OptionGroup> options,
        List<VariantInfo> variants
) {

    public record OptionGroup(
            String code,
            String name,
            List<OptionValue> values
    ) {
    }

    public record OptionValue(
            Long id,
            String value,
            String colorHex
    ) {
    }

    public record VariantInfo(
            Long id,
            String sku,
            String label,
            BigDecimal price,
            int stockQuantity,
            List<Long> optionValueIds
    ) {
    }
}