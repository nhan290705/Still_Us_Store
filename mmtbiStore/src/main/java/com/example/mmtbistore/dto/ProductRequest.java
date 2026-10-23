package com.example.mmtbistore.dto;

import com.example.mmtbistore.model.enums.ProductStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 200, message = "Tên sản phẩm không được quá 200 ký tự")
    private String name;

    @NotBlank(message = "Slug sản phẩm không được để trống")
    @Size(max = 220, message = "Slug sản phẩm không được quá 220 ký tự")
    private String slug;

    @NotBlank(message = "Mã sản phẩm (code) không được để trống")
    @Size(max = 30, message = "Mã sản phẩm không được quá 30 ký tự")
    private String code;

    @NotNull(message = "Danh mục không được để trống")
    private Long categoryId;

    @Size(max = 500, message = "Mô tả ngắn không được quá 500 ký tự")
    private String shortDescription;

    private String description;

    @NotNull(message = "Giá cơ bản không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá cơ bản không được âm")
    private BigDecimal basePrice;

    @Size(max = 500, message = "Đường dẫn ảnh mockup không quá 500 ký tự")
    private String mockupImageUrl;

    @JsonProperty("isCustomizable")
    private Boolean isCustomizable = true;

    @JsonProperty("isCoupleBox")
    private Boolean isCoupleBox = false;

    @Min(value = 0, message = "Khối lượng gói hàng không được âm")
    private Integer weightGrams;

    private ProductStatus status = ProductStatus.DRAFT;
}
