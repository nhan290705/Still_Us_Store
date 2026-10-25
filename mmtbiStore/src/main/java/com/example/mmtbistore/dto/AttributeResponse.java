package com.example.mmtbistore.dto;

import com.example.mmtbistore.model.enums.AttributeDisplayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeResponse {
    private Long id;
    private String name;
    private String code;
    private AttributeDisplayType displayType;
    private Instant createdAt;
    private Instant updatedAt;

    @Builder.Default
    private List<AttributeValueResponse> values = new ArrayList<>();
}
