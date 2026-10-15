package com.example.mmtbistore.model.memory;

import com.example.mmtbistore.model.common.BaseEntity;
import com.example.mmtbistore.model.enums.VoucherStatus;
import com.example.mmtbistore.model.enums.VoucherType;
import com.example.mmtbistore.model.user.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

// Ưu đãi mốc STILL US, áp dụng trên web:
// 90 ngày tặng 1 pack charm (FREE_GIFT), 180 ngày giảm 20% đơn từ 899.000đ, 365 ngày giảm 30% toàn shop
@Getter @Setter
@Entity
@Table(name = "vouchers")
public class Voucher extends BaseEntity {

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    // Chỉ chủ voucher được dùng: SĐT đặt hàng phải khớp
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_page_id")  // sinh ra từ chuỗi STILL US nào
    private MemoryPage memoryPage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VoucherType type;

    @Column(precision = 18, scale = 2)
    private BigDecimal value;            // 20 hoặc 30 (%); null với FREE_GIFT

    @Column(precision = 18, scale = 2)
    private BigDecimal minOrderAmount;   // 899000 với mốc 180 ngày

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VoucherStatus status = VoucherStatus.ACTIVE;

    private Instant validUntil;          // null = chưa có hạn (chờ khách chốt)
    private Instant usedAt;
}
