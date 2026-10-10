package com.example.mmtbistore.model.customization;

import com.example.mmtbistore.model.catalog.Product;
import com.example.mmtbistore.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Getter @Setter
@Entity
@Table(name = "design_templates")
public class DesignTemplate extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")     // null = dùng chung mọi sản phẩm
    private Product product;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 500)
    private String thumbnailUrl;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String layersJson = "[]";

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal extraFee = BigDecimal.ZERO;

    private boolean isActive = true;
}
