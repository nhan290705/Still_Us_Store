package com.example.mmtbistore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ProductVariantRequest {

    @NotBlank(message = "Mã SKU không được để trống")
    @Size(max = 50, message = "Mã SKU không quá 50 ký tự")
    private String sku;

    private BigDecimal priceAdjustment = BigDecimal.ZERO;

    @Min(value = 0, message = "Số lượng tồn kho không được âm")
    private int stockQuantity = 0;

    @Size(max = 500, message = "Đường dẫn ảnh biến thể không quá 500 ký tự")
    private String imageUrl;

    @JsonProperty("isActive")
    private Boolean isActive = true;

    private Set<Long> attributeValueIds = new HashSet<>();
}
