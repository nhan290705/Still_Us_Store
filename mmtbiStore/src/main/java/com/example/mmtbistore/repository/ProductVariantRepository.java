package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.catalog.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    Optional<ProductVariant> findBySku(String sku);
    boolean existsBySku(String sku);
    boolean existsBySkuAndIdNot(String sku, Long id);
    List<ProductVariant> findByProduct_Id(Long productId);

    // Trừ kho an toàn khi đặt hàng: trả về 0 nếu không đủ hàng
    @Modifying
    @Query("""
           update ProductVariant v
           set v.stockQuantity = v.stockQuantity - :qty
           where v.id = :id and v.stockQuantity >= :qty
           """)
    int decreaseStock(@Param("id") Long id, @Param("qty") int qty);

    // Hoàn kho khi đơn bị huỷ / hết hạn thanh toán
    @Modifying
    @Query("update ProductVariant v set v.stockQuantity = v.stockQuantity + :qty where v.id = :id")
    int increaseStock(@Param("id") Long id, @Param("qty") int qty);
}
