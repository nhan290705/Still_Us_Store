package com.example.mmtbistore.dto;

import com.example.mmtbistore.model.enums.AttributeDisplayType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeRequest {

    @NotBlank(message = "Tên thuộc tính không được để trống")
    @Size(max = 50, message = "Tên thuộc tính không quá 50 ký tự")
    private String name;

    @NotBlank(message = "Mã thuộc tính (code) không được để trống")
    @Size(max = 30, message = "Mã thuộc tính không quá 30 ký tự")
    private String code;

    @NotNull(message = "Kiểu hiển thị displayType không được để trống")
    private AttributeDisplayType displayType;
}
