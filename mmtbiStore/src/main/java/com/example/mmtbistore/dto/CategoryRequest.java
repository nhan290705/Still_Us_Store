package com.example.mmtbistore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(max = 100, message = "Tên danh mục không được quá 100 ký tự")
    private String name;

    @NotBlank(message = "Slug không được để trống")
    @Size(max = 120, message = "Slug không được quá 120 ký tự")
    private String slug;

    @Size(max = 500, message = "Đường dẫn ảnh không được quá 500 ký tự")
    private String imageUrl;

    private Long parentId;

    private Integer sortOrder = 0;

    @JsonProperty("isActive")
    private Boolean isActive = true;
}
