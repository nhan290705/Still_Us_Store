package com.example.mmtbistore.model.catalog;


import com.example.mmtbistore.model.common.BaseEntity;
import com.example.mmtbistore.model.enums.AttributeDisplayType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "product_attributes")
public class ProductAttribute extends BaseEntity {
    @Column(nullable = false, length = 50)
    private String name;                 // "Kích cỡ"

    @Column(nullable = false, unique = true, length = 30)
    private String code;                 // "size"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AttributeDisplayType displayType;

    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttributeValue> values = new ArrayList<>();

}
