package com.example.mmtbistore.service;

import com.example.mmtbistore.dto.ProductImageRequest;
import com.example.mmtbistore.dto.ProductImageResponse;
import com.example.mmtbistore.exception.ResourceNotFoundException;
import com.example.mmtbistore.model.catalog.Product;
import com.example.mmtbistore.model.catalog.ProductImage;
import com.example.mmtbistore.repository.ProductImageRepository;
import com.example.mmtbistore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProductImageService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    @Transactional(readOnly = true)
    public List<ProductImageResponse> getProductImages(Long productId) {
        validateProductExists(productId);
        return productImageRepository.findByProduct_IdOrderBySortOrderAsc(productId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProductImageResponse addImage(Long productId, ProductImageRequest request) {
        Product product = validateProductExists(productId);

        boolean isPrimary = Boolean.TRUE.equals(request.getIsPrimary());
        if (isPrimary) {
            demoteOtherPrimaryImages(productId);
        }

        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setUrl(request.getUrl().trim());
        image.setAltText(request.getAltText() != null ? request.getAltText().trim() : null);
        image.setSortOrder(request.getSortOrder());
        image.setPrimary(isPrimary);

        ProductImage saved = productImageRepository.save(image);
        return toResponse(saved);
    }

    @Transactional
    public ProductImageResponse updateImage(Long imageId, ProductImageRequest request) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ảnh với ID: " + imageId));

        boolean isPrimary = Boolean.TRUE.equals(request.getIsPrimary());
        if (isPrimary && !image.isPrimary()) {
            demoteOtherPrimaryImages(image.getProduct().getId());
        }

        image.setUrl(request.getUrl().trim());
        image.setAltText(request.getAltText() != null ? request.getAltText().trim() : null);
        image.setSortOrder(request.getSortOrder());
        image.setPrimary(isPrimary);

        ProductImage saved = productImageRepository.save(image);
        return toResponse(saved);
    }

    @Transactional
    public void deleteImage(Long imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ảnh với ID: " + imageId));

        productImageRepository.delete(image);
    }

    private Product validateProductExists(Long productId) {
        return productRepository.findById(productId)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + productId));
    }

    private void demoteOtherPrimaryImages(Long productId) {
        List<ProductImage> existing = productImageRepository.findByProduct_IdOrderBySortOrderAsc(productId);
        boolean changed = false;
        for (ProductImage img : existing) {
            if (img.isPrimary()) {
                img.setPrimary(false);
                changed = true;
            }
        }
        if (changed) {
            productImageRepository.saveAll(existing);
        }
    }

    public ProductImageResponse toResponse(ProductImage img) {
        return ProductImageResponse.builder()
                .id(img.getId())
                .productId(img.getProduct() != null ? img.getProduct().getId() : null)
                .url(img.getUrl())
                .altText(img.getAltText())
                .sortOrder(img.getSortOrder())
                .isPrimary(img.isPrimary())
                .createdAt(img.getCreatedAt())
                .updatedAt(img.getUpdatedAt())
                .build();
    }
}
