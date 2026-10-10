package com.example.mmtbistore.model.catalog;


import com.example.mmtbistore.model.common.BaseEntity;
import com.example.mmtbistore.model.enums.DiscountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "product_discounts",
        indexes = @Index(name = "ix_discounts_product_time", columnList = "product_id, start_at, end_at"))
public class ProductDiscount extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, length = 100)
    private String name;                 // "Sale Valentine"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DiscountType type;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal value;            // 15 (%) hoặc 50000 (đ)

    @Column(precision = 18, scale = 2)
    private BigDecimal maxDiscountAmount;

    @Column(nullable = false)
    private Instant startAt;

    @Column(nullable = false)
    private Instant endAt;

    private boolean isActive = true;
}
