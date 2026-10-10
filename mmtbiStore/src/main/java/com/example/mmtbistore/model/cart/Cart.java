package com.example.mmtbistore.model.cart;

import com.example.mmtbistore.model.enums.CartStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;                     // = cartToken lưu ở localStorage

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CartStatus status = CartStatus.ACTIVE;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    // Tự set trong service mỗi khi thêm/sửa/xoá item: cart.setUpdatedAt(Instant.now())
    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();
}
