package com.example.mmtbistore.dto;

import com.example.mmtbistore.model.enums.UserRole;
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
public class StaffResponse {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private UserRole role;

    @JsonProperty("isActive")
    private boolean isActive;

    private Instant lastLoginAt;
    private Instant createdAt;
    private Instant updatedAt;
}
