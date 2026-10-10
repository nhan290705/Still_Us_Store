package com.example.mmtbistore.model.catalog;


import com.example.mmtbistore.model.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "product_variants")
public class ProductVariant extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal priceAdjustment = BigDecimal.ZERO;   // +20.000 cho size XXL

    @Min(0)
    private int stockQuantity;

    @Column(length = 500)
    private String imageUrl;

    private boolean isActive = true;

    // n-n -> bảng nối product_variant_attribute_values
    @ManyToMany
    @JoinTable(name = "product_variant_attribute_values",
            joinColumns = @JoinColumn(name = "variant_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_value_id"))
    private Set<AttributeValue> attributeValues = new HashSet<>();
}
