package com.example.mmtbistore.dto;

import com.example.mmtbistore.model.enums.ProductStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductCardResponse {
    private Long id;
    private String code;
    private String name;
    private String slug;
    private Long categoryId;
    private String categoryName;
    private BigDecimal basePrice;
    private String mockupImageUrl;
    private ProductStatus status;

    @JsonProperty("isCoupleBox")
    private Boolean isCoupleBox;

    private Integer weightGrams;

    @JsonProperty("isCustomizable")
    private boolean isCustomizable;

    @JsonProperty("isDeleted")
    private boolean isDeleted;

    private int variantCount;
    private int totalStock;
    private double averageRating;
    private int reviewCount;
    private int soldCount;
    private Instant createdAt;
    private Instant updatedAt;
}
