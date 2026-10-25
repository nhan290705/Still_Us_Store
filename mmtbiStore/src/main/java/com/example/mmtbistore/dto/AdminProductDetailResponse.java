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
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductDetailResponse {
    private Long id;
    private String code;
    private String name;
    private String slug;
    private Long categoryId;
    private String categoryName;
    private String shortDescription;
    private String description;
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

    private double averageRating;
    private int reviewCount;
    private int soldCount;
    private Instant createdAt;
    private Instant updatedAt;

    @Builder.Default
    private List<ProductImageResponse> images = new ArrayList<>();

    @Builder.Default
    private List<AdminProductVariantResponse> variants = new ArrayList<>();
}
