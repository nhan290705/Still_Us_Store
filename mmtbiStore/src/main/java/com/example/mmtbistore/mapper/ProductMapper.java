package com.example.mmtbistore.mapper;

import com.example.mmtbistore.dto.ProductCardResponse;
import com.example.mmtbistore.dto.ProductDetailResponse;
import com.example.mmtbistore.model.catalog.AttributeValue;
import com.example.mmtbistore.model.catalog.Category;
import com.example.mmtbistore.model.catalog.Product;
import com.example.mmtbistore.model.catalog.ProductAttribute;
import com.example.mmtbistore.model.catalog.ProductImage;
import com.example.mmtbistore.model.catalog.ProductVariant;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    // ===================== Thẻ sản phẩm (Bài 3) =====================

    public ProductCardResponse toCard(Product product) {
        List<ProductVariant> variants = activeVariants(product);

        return new ProductCardResponse(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getShortDescription(),
                lowestPrice(product, variants),
                primaryImageUrl(product),
                hasStock(variants)
        );
    }

    // ===================== Trang chi tiết (MỚI Bài 4) =====================

    public ProductDetailResponse toDetail(Product product) {
        List<ProductVariant> variants = activeVariants(product);
        Category category = product.getCategory();

        List<ProductDetailResponse.VariantInfo> variantInfos = variants.stream()
                .map(v -> toVariantInfo(product, v))
                .toList();

        return new ProductDetailResponse(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getShortDescription(),
                product.getDescription(),
                product.getBasePrice(),
                category.getName(),
                category.getSlug(),
                hasStock(variants),
                imageUrls(product),
                buildOptions(variants),
                variantInfos
        );
    }

    private List<ProductDetailResponse.OptionGroup> buildOptions(List<ProductVariant> variants) {
        // Bước 1: gom giá trị theo thuộc tính. Key = id thuộc tính (TreeMap tự sắp id tăng dần)
        Map<Long, List<AttributeValue>> valuesByAttribute = new TreeMap<>();
        for (ProductVariant variant : variants) {
            for (AttributeValue value : variant.getAttributeValues()) {
                Long attributeId = value.getAttribute().getId();
                List<AttributeValue> list = valuesByAttribute.computeIfAbsent(attributeId, id -> new ArrayList<>());
                if (!list.contains(value)) {
                    list.add(value);
                }
            }
        }

        // Bước 2: mỗi thuộc tính thành 1 nhóm lựa chọn, giá trị sắp theo sortOrder
        List<ProductDetailResponse.OptionGroup> groups = new ArrayList<>();
        for (List<AttributeValue> values : valuesByAttribute.values()) {
            ProductAttribute attribute = values.get(0).getAttribute();

            List<ProductDetailResponse.OptionValue> optionValues = values.stream()
                    .sorted(Comparator.comparingInt(AttributeValue::getSortOrder))
                    .map(v -> new ProductDetailResponse.OptionValue(v.getId(), v.getValue(), v.getColorHex()))
                    .toList();

            groups.add(new ProductDetailResponse.OptionGroup(attribute.getCode(), attribute.getName(), optionValues));
        }
        return groups;
    }

    private ProductDetailResponse.VariantInfo toVariantInfo(Product product, ProductVariant variant) {
        List<AttributeValue> values = variant.getAttributeValues().stream()
                .sorted(Comparator.comparing((AttributeValue v) -> v.getAttribute().getId()))
                .toList();

        String label = values.stream()
                .map(AttributeValue::getValue)
                .collect(Collectors.joining(" / "));

        List<Long> valueIds = values.stream()
                .map(AttributeValue::getId)
                .toList();

        return new ProductDetailResponse.VariantInfo(
                variant.getId(),
                variant.getSku(),
                label,
                product.getBasePrice().add(variant.getPriceAdjustment()),
                variant.getStockQuantity(),
                valueIds
        );
    }

    private List<ProductVariant> activeVariants(Product product) {
        return product.getVariants().stream()
                .filter(ProductVariant::isActive)
                .sorted(Comparator.comparing(ProductVariant::getId))
                .toList();
    }

    private BigDecimal lowestPrice(Product product, List<ProductVariant> variants) {
        return variants.stream()
                .map(v -> product.getBasePrice().add(v.getPriceAdjustment()))
                .min(Comparator.naturalOrder())
                .orElse(product.getBasePrice());
    }

    private boolean hasStock(List<ProductVariant> variants) {
        return variants.stream().anyMatch(v -> v.getStockQuantity() > 0);
    }

    private List<String> imageUrls(Product product) {
        return product.getImages().stream()
                .sorted(Comparator.comparing(ProductImage::isPrimary).reversed()
                        .thenComparingInt(ProductImage::getSortOrder))
                .map(ProductImage::getUrl)
                .toList();
    }

    private String primaryImageUrl(Product product) {
        return imageUrls(product).stream()
                .findFirst()
                .orElse(product.getMockupImageUrl());
    }
}