package com.example.mmtbistore.model.catalog;


import com.example.mmtbistore.model.common.BaseEntity;
import com.example.mmtbistore.model.customization.CustomizationArea;
import com.example.mmtbistore.model.enums.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products",
        indexes = @Index(name = "ix_products_status", columnList = "status, is_deleted"))
public class Product extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, unique = true, length = 220)
    private String slug;

    @Column(length = 500)
    private String shortDescription;

    @Column(columnDefinition = "text")
    private String description;

    @Min(0)
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal basePrice;

    @Column(length = 500)
    private String mockupImageUrl;       // ảnh phôi để vẽ canvas custom

    private boolean isCustomizable = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status = ProductStatus.DRAFT;

    private boolean isDeleted;           // xoá mềm
    private double averageRating;
    private int reviewCount;
    private int soldCount;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> variants = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDiscount> discounts = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomizationArea> customizationAreas = new ArrayList<>();
}
