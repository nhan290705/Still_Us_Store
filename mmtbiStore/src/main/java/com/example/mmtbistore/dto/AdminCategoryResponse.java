package com.example.mmtbistore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class AdminCategoryResponse {
    private Long id;
    private String name;
    private String slug;
    private String imageUrl;
    private Long parentId;
    private String parentName;
    private int sortOrder;

    @JsonProperty("isActive")
    private boolean isActive;

    private Instant createdAt;
    private Instant updatedAt;

    @Builder.Default
    private List<AdminCategoryResponse> children = new ArrayList<>();
}
