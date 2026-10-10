package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.catalog.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
