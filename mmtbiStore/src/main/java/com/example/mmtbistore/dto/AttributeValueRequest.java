package com.example.mmtbistore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeValueRequest {

    @NotBlank(message = "Giá trị thuộc tính không được để trống")
    @Size(max = 50, message = "Giá trị không quá 50 ký tự")
    private String value;

    @Size(max = 7, message = "Mã màu hex không quá 7 ký tự (VD: #RRGGBB)")
    private String colorHex;

    private Integer sortOrder = 0;
}
