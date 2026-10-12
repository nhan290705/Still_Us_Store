package com.example.mmtbistore.service;

import com.example.mmtbistore.dto.PageResponse;
import com.example.mmtbistore.dto.ProductCardResponse;
import com.example.mmtbistore.dto.ProductDetailResponse;
import com.example.mmtbistore.exception.ResourceNotFoundException;
import com.example.mmtbistore.mapper.ProductMapper;
import com.example.mmtbistore.model.catalog.Product;
import com.example.mmtbistore.model.enums.ProductStatus;
import com.example.mmtbistore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final int MAX_PAGE_SIZE = 50;

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public PageResponse<ProductCardResponse> getProducts(String categorySlug, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(safePage, safeSize,
                Sort.by(Sort.Direction.DESC, "createdAt", "id"));

        Page<Product> products;
        if (categorySlug == null || categorySlug.isBlank()) {
            products = productRepository.findByStatusAndIsDeletedFalse(ProductStatus.ACTIVE, pageable);
        } else {
            products = productRepository.findByStatusAndIsDeletedFalseAndCategory_Slug(
                    ProductStatus.ACTIVE, categorySlug, pageable);
        }

        return PageResponse.from(products.map(productMapper::toCard));
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(String slug) {
        Product product = productRepository.findBySlugAndStatusAndIsDeletedFalse(slug, ProductStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm: " + slug));
        return productMapper.toDetail(product);
    }
}
