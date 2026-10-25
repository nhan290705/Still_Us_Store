package com.example.mmtbistore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductVariantResponse {
    private Long id;
    private Long productId;
    private String sku;
    private BigDecimal priceAdjustment;
    private BigDecimal finalPrice;
    private int stockQuantity;
    private String imageUrl;

    @JsonProperty("isActive")
    private boolean isActive;

    private Instant createdAt;
    private Instant updatedAt;

    @Builder.Default
    private List<AttributeValueResponse> attributeValues = new ArrayList<>();
}
