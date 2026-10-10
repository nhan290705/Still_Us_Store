package com.example.mmtbistore.model.user;

import com.example.mmtbistore.model.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {
    @Column(nullable = false, unique = true, length = 15)
    private String phoneNumber;          // 1 SĐT = 1 khách

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(length = 100)
    private String email;
}
