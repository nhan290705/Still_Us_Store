package com.example.mmtbistore.controller;

import com.example.mmtbistore.dto.PageResponse;
import com.example.mmtbistore.dto.ProductCardResponse;
import com.example.mmtbistore.dto.ProductDetailResponse;
import com.example.mmtbistore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public PageResponse<ProductCardResponse> getProducts(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "12") int size) {
        return productService.getProducts(category, page, size);
    }

    @GetMapping("/{slug}")
    public ProductDetailResponse getProduct(@PathVariable("slug") String slug) {
        return productService.getProductDetail(slug);
    }
}