package com.example.mmtbistore.model.catalog;

import com.example.mmtbistore.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "product_images")
public class ProductImage extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(length = 200)
    private String altText;

    private int sortOrder;
    private boolean isPrimary;
}
