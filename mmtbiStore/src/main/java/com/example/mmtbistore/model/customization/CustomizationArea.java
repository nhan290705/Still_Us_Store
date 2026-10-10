package com.example.mmtbistore.model.customization;

import com.example.mmtbistore.model.catalog.Product;
import com.example.mmtbistore.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "customization_areas")
public class CustomizationArea extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, length = 100)
    private String name;                 // "Mặt trước", "Mặt sau"

    @Column(length = 500)
    private String backgroundImageUrl;

    // Khung được phép vẽ trên ảnh mockup (px)
    private int positionX;
    private int positionY;
    private int width;
    private int height;

    private boolean allowText = true;
    private boolean allowIcon = true;
    private boolean allowImage;
    private boolean allowTemplate = true;
    private int maxTextLength = 30;
    private int maxLayers = 10;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal extraFee = BigDecimal.ZERO;

    private int sortOrder;
}
