package com.example.mmtbistore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageRequest {

    @NotBlank(message = "Đường dẫn ảnh không được để trống")
    @Size(max = 500, message = "Đường dẫn ảnh không được quá 500 ký tự")
    private String url;

    @Size(max = 200, message = "Văn bản thay thế (altText) không được quá 200 ký tự")
    private String altText;

    private int sortOrder = 0;

    @JsonProperty("isPrimary")
    private Boolean isPrimary = false;
}
