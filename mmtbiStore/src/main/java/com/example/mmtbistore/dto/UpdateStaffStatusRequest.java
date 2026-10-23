package com.example.mmtbistore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStaffStatusRequest {

    @NotNull(message = "Trạng thái isActive không được để trống")
    @JsonProperty("isActive")
    private Boolean isActive;
}
