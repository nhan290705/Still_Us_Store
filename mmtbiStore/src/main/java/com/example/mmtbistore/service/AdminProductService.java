package com.example.mmtbistore.service;

import com.example.mmtbistore.dto.*;
import com.example.mmtbistore.exception.BadRequestException;
import com.example.mmtbistore.exception.ConflictException;
import com.example.mmtbistore.exception.ResourceNotFoundException;
import com.example.mmtbistore.model.catalog.AttributeValue;
import com.example.mmtbistore.model.catalog.Category;
import com.example.mmtbistore.model.catalog.Product;
import com.example.mmtbistore.model.catalog.ProductImage;
import com.example.mmtbistore.model.catalog.ProductVariant;
import com.example.mmtbistore.model.enums.ProductStatus;
import com.example.mmtbistore.repository.AttributeValueRepository;
import com.example.mmtbistore.repository.CategoryRepository;
import com.example.mmtbistore.repository.ProductRepository;
import com.example.mmtbistore.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CategoryRepository categoryRepository;
    private final AttributeValueRepository attributeValueRepository;

    @Transactional(readOnly = true)
    public PageResponse<AdminProductCardResponse> getProducts(
            int page,
            int size,
            String search,
            ProductStatus status,
            Long categoryId
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        String keyword = (search != null && !search.trim().isEmpty()) ? search.trim() : null;

        Page<Product> productPage = productRepository.searchAdminProducts(keyword, status, categoryId, pageable);
        return PageResponse.from(productPage.map(this::toCardResponse));
    }

    @Transactional(readOnly = true)
    public AdminProductDetailResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));

        return toDetailResponse(product);
    }

    @Transactional
    public AdminProductDetailResponse createProduct(ProductRequest request) {
        validatePriceAndStock(request.getBasePrice());

        String slug = request.getSlug().trim();
        if (productRepository.existsBySlug(slug)) {
            throw new ConflictException("Slug sản phẩm '" + slug + "' đã tồn tại");
        }

        String code = request.getCode().trim();
        if (productRepository.existsByCode(code)) {
            throw new ConflictException("Mã sản phẩm '" + code + "' đã tồn tại");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + request.getCategoryId()));

        Product product = new Product();
        product.setName(request.getName().trim());
        product.setSlug(slug);
        product.setCode(code);
        product.setCategory(category);
        product.setShortDescription(request.getShortDescription() != null ? request.getShortDescription().trim() : null);
        product.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);
        product.setBasePrice(request.getBasePrice());
        product.setMockupImageUrl(request.getMockupImageUrl() != null ? request.getMockupImageUrl().trim() : null);
        product.setCustomizable(request.getIsCustomizable() != null ? request.getIsCustomizable() : true);
        product.setIsCoupleBox(request.getIsCoupleBox() != null ? request.getIsCoupleBox() : false);
        product.setWeightGrams(request.getWeightGrams() != null ? request.getWeightGrams() : 0);
        product.setStatus(request.getStatus() != null ? request.getStatus() : ProductStatus.DRAFT);
        product.setDeleted(false);

        Product saved = productRepository.save(product);
        return toDetailResponse(saved);
    }

    @Transactional
    public AdminProductDetailResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));

        validatePriceAndStock(request.getBasePrice());

        String slug = request.getSlug().trim();
        if (productRepository.existsBySlugAndIdNot(slug, id)) {
            throw new ConflictException("Slug sản phẩm '" + slug + "' đã tồn tại");
        }

        String code = request.getCode().trim();
        if (productRepository.existsByCodeAndIdNot(code, id)) {
            throw new ConflictException("Mã sản phẩm '" + code + "' đã tồn tại");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + request.getCategoryId()));

        product.setName(request.getName().trim());
        product.setSlug(slug);
        product.setCode(code);
        product.setCategory(category);
        product.setShortDescription(request.getShortDescription() != null ? request.getShortDescription().trim() : null);
        product.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);
        product.setBasePrice(request.getBasePrice());
        product.setMockupImageUrl(request.getMockupImageUrl() != null ? request.getMockupImageUrl().trim() : null);
        product.setCustomizable(request.getIsCustomizable() != null ? request.getIsCustomizable() : true);
        product.setIsCoupleBox(request.getIsCoupleBox() != null ? request.getIsCoupleBox() : false);
        product.setWeightGrams(request.getWeightGrams() != null ? request.getWeightGrams() : 0);
        if (request.getStatus() != null) {
            product.setStatus(request.getStatus());
        }

        Product saved = productRepository.save(product);
        return toDetailResponse(saved);
    }

    @Transactional
    public AdminProductDetailResponse updateStatus(Long id, ProductStatus status) {
        Product product = productRepository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));

        product.setStatus(status);
        Product saved = productRepository.save(product);
        return toDetailResponse(saved);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));

        // Xoá mềm theo yêu cầu kiến trúc (không xoá cứng vì còn tham chiếu đơn hàng)
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Transactional
    public AdminProductVariantResponse addVariant(Long productId, ProductVariantRequest request) {
        Product product = productRepository.findById(productId)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + productId));

        if (request.getStockQuantity() < 0) {
            throw new BadRequestException("Số lượng tồn kho không được âm: " + request.getStockQuantity());
        }

        BigDecimal adjustment = request.getPriceAdjustment() != null ? request.getPriceAdjustment() : BigDecimal.ZERO;
        BigDecimal finalPrice = product.getBasePrice().add(adjustment);
        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Giá biến thể sau điều chỉnh không được âm: " + finalPrice);
        }

        String sku = request.getSku().trim();
        if (productVariantRepository.existsBySku(sku)) {
            throw new ConflictException("Mã SKU biến thể '" + sku + "' đã tồn tại");
        }

        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setSku(sku);
        variant.setPriceAdjustment(adjustment);
        variant.setStockQuantity(request.getStockQuantity());
        variant.setImageUrl(request.getImageUrl() != null ? request.getImageUrl().trim() : null);
        variant.setActive(request.getIsActive() != null ? request.getIsActive() : true);

        if (request.getAttributeValueIds() != null && !request.getAttributeValueIds().isEmpty()) {
            List<AttributeValue> values = attributeValueRepository.findAllById(request.getAttributeValueIds());
            variant.setAttributeValues(new HashSet<>(values));
        }

        ProductVariant saved = productVariantRepository.save(variant);
        return toVariantResponse(saved, product.getBasePrice());
    }

    @Transactional
    public AdminProductVariantResponse updateVariant(Long variantId, ProductVariantRequest request) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy biến thể với ID: " + variantId));

        if (request.getStockQuantity() < 0) {
            throw new BadRequestException("Số lượng tồn kho không được âm: " + request.getStockQuantity());
        }

        BigDecimal adjustment = request.getPriceAdjustment() != null ? request.getPriceAdjustment() : BigDecimal.ZERO;
        BigDecimal finalPrice = variant.getProduct().getBasePrice().add(adjustment);
        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Giá biến thể sau điều chỉnh không được âm: " + finalPrice);
        }

        String sku = request.getSku().trim();
        if (productVariantRepository.existsBySkuAndIdNot(sku, variantId)) {
            throw new ConflictException("Mã SKU biến thể '" + sku + "' đã tồn tại");
        }

        variant.setSku(sku);
        variant.setPriceAdjustment(adjustment);
        variant.setStockQuantity(request.getStockQuantity());
        variant.setImageUrl(request.getImageUrl() != null ? request.getImageUrl().trim() : null);
        variant.setActive(request.getIsActive() != null ? request.getIsActive() : true);

        if (request.getAttributeValueIds() != null) {
            List<AttributeValue> values = attributeValueRepository.findAllById(request.getAttributeValueIds());
            variant.setAttributeValues(new HashSet<>(values));
        }

        ProductVariant saved = productVariantRepository.save(variant);
        return toVariantResponse(saved, variant.getProduct().getBasePrice());
    }

    @Transactional
    public void deleteVariant(Long variantId) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy biến thể với ID: " + variantId));

        productVariantRepository.delete(variant);
    }

    private void validatePriceAndStock(BigDecimal basePrice) {
        if (basePrice == null || basePrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Giá sản phẩm không được âm: " + basePrice);
        }
    }

    private AdminProductCardResponse toCardResponse(Product p) {
        int variantCount = p.getVariants() != null ? p.getVariants().size() : 0;
        int totalStock = p.getVariants() != null ? p.getVariants().stream().mapToInt(ProductVariant::getStockQuantity).sum() : 0;

        return AdminProductCardResponse.builder()
                .id(p.getId())
                .code(p.getCode())
                .name(p.getName())
                .slug(p.getSlug())
                .categoryId(p.getCategory() != null ? p.getCategory().getId() : null)
                .categoryName(p.getCategory() != null ? p.getCategory().getName() : null)
                .basePrice(p.getBasePrice())
                .mockupImageUrl(p.getMockupImageUrl())
                .status(p.getStatus())
                .isCoupleBox(p.getIsCoupleBox())
                .weightGrams(p.getWeightGrams())
                .isCustomizable(p.isCustomizable())
                .isDeleted(p.isDeleted())
                .variantCount(variantCount)
                .totalStock(totalStock)
                .averageRating(p.getAverageRating())
                .reviewCount(p.getReviewCount())
                .soldCount(p.getSoldCount())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    private AdminProductDetailResponse toDetailResponse(Product p) {
        List<ProductImageResponse> imageResponses = (p.getImages() != null) ? p.getImages().stream()
                .map(this::toImageResponse)
                .toList() : List.of();

        List<AdminProductVariantResponse> variantResponses = (p.getVariants() != null) ? p.getVariants().stream()
                .map(v -> toVariantResponse(v, p.getBasePrice()))
                .toList() : List.of();

        return AdminProductDetailResponse.builder()
                .id(p.getId())
                .code(p.getCode())
                .name(p.getName())
                .slug(p.getSlug())
                .categoryId(p.getCategory() != null ? p.getCategory().getId() : null)
                .categoryName(p.getCategory() != null ? p.getCategory().getName() : null)
                .shortDescription(p.getShortDescription())
                .description(p.getDescription())
                .basePrice(p.getBasePrice())
                .mockupImageUrl(p.getMockupImageUrl())
                .status(p.getStatus())
                .isCoupleBox(p.getIsCoupleBox())
                .weightGrams(p.getWeightGrams())
                .isCustomizable(p.isCustomizable())
                .isDeleted(p.isDeleted())
                .averageRating(p.getAverageRating())
                .reviewCount(p.getReviewCount())
                .soldCount(p.getSoldCount())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .images(imageResponses)
                .variants(variantResponses)
                .build();
    }

    private ProductImageResponse toImageResponse(ProductImage img) {
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

    private AdminProductVariantResponse toVariantResponse(ProductVariant v, BigDecimal basePrice) {
        BigDecimal finalPrice = (basePrice != null && v.getPriceAdjustment() != null)
                ? basePrice.add(v.getPriceAdjustment())
                : basePrice;

        List<AttributeValueResponse> valueResponses = (v.getAttributeValues() != null) ? v.getAttributeValues().stream()
                .map(val -> AttributeValueResponse.builder()
                        .id(val.getId())
                        .attributeId(val.getAttribute() != null ? val.getAttribute().getId() : null)
                        .value(val.getValue())
                        .colorHex(val.getColorHex())
                        .sortOrder(val.getSortOrder())
                        .createdAt(val.getCreatedAt())
                        .updatedAt(val.getUpdatedAt())
                        .build())
                .toList() : List.of();

        return AdminProductVariantResponse.builder()
                .id(v.getId())
                .productId(v.getProduct() != null ? v.getProduct().getId() : null)
                .sku(v.getSku())
                .priceAdjustment(v.getPriceAdjustment())
                .finalPrice(finalPrice)
                .stockQuantity(v.getStockQuantity())
                .imageUrl(v.getImageUrl())
                .isActive(v.isActive())
                .createdAt(v.getCreatedAt())
                .updatedAt(v.getUpdatedAt())
                .attributeValues(valueResponses)
                .build();
    }
}
