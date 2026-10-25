package com.example.mmtbistore.dto;

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
public class AttributeValueResponse {
    private Long id;
    private Long attributeId;
    private String value;
    private String colorHex;
    private int sortOrder;
    private Instant createdAt;
    private Instant updatedAt;
}
