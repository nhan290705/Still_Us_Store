package com.example.mmtbistore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageResponse {
    private Long id;
    private Long productId;
    private String url;
    private String altText;
    private int sortOrder;

    @JsonProperty("isPrimary")
    private boolean isPrimary;

    private Instant createdAt;
    private Instant updatedAt;
}
