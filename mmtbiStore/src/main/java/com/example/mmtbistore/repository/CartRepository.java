package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {   // id là UUID
}

