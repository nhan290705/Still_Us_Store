package com.example.mmtbistore.dto;

import com.example.mmtbistore.model.enums.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductStatusRequest {

    @NotNull(message = "Trạng thái sản phẩm không được để trống")
    private ProductStatus status;
}
