package com.example.mmtbistore.model.catalog;

import com.example.mmtbistore.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "attribute_values",
        uniqueConstraints = @UniqueConstraint(columnNames = {"attribute_id", "value"}))

public class AttributeValue extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attribute_id", nullable = false)
    private ProductAttribute attribute;

    @Column(nullable = false, length = 50)
    private String value;                // "L", "Đen"

    @Column(length = 7)
    private String colorHex;             // "#000000"

    private int sortOrder;
}
