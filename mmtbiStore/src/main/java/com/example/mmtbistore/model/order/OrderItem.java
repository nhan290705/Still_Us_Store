package com.example.mmtbistore.model.order;

import com.example.mmtbistore.model.catalog.Product;
import com.example.mmtbistore.model.catalog.ProductVariant;
import com.example.mmtbistore.model.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Getter @Setter
@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;

    // ----- Snapshot lúc đặt hàng -----
    @Column(nullable = false, length = 200) private String productName;
    @Column(length = 500)                   private String productImageUrl;
    @Column(nullable = false, length = 100) private String variantLabel;   // "Size M / Trắng"
    @Column(nullable = false, length = 50)  private String sku;

    @Min(1)
    private int quantity;

    @Column(nullable = false, precision = 18, scale = 2) private BigDecimal unitPrice;   // base_price + price_adjustment
    @Column(nullable = false, precision = 18, scale = 2) private BigDecimal unitDiscount = BigDecimal.ZERO;
    @Column(nullable = false, precision = 18, scale = 2) private BigDecimal unitCustomizationFee = BigDecimal.ZERO;
    @Column(nullable = false, precision = 18, scale = 2) private BigDecimal lineTotal;

    private Long appliedDiscountId;      // không FK

    // ----- Thiết kế đã chốt -----
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String designJson;

    @Column(length = 500) private String previewImageUrl;
    @Column(length = 500) private String printFileUrl;
}