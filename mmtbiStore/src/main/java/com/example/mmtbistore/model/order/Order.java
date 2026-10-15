package com.example.mmtbistore.model.order;

import com.example.mmtbistore.model.common.BaseEntity;
import com.example.mmtbistore.model.memory.Voucher;
import com.example.mmtbistore.model.enums.OrderStatus;
import com.example.mmtbistore.model.enums.PaymentMethod;
import com.example.mmtbistore.model.enums.PaymentStatus;
import com.example.mmtbistore.model.enums.SalesChannel;
import com.example.mmtbistore.model.user.AppUser;
import com.example.mmtbistore.model.user.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
@Entity
@Table(name = "orders", indexes = {
        @Index(name = "ix_orders_customer_created", columnList = "customer_id, created_at"),
        @Index(name = "ix_orders_status", columnList = "status")
})
public class Order extends BaseEntity {

    @Column(nullable = false, unique = true, length = 30)
    private String orderCode;            // "DD261009-7K3FQ"

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;           // người đặt

    // ----- Người nhận -----
    @Column(nullable = false, length = 100) private String receiverName;
    @Column(nullable = false, length = 15)  private String receiverPhone;
    @Column(length = 100)                   private String email;
    @Column(nullable = false, length = 100) private String province;
    @Column(nullable = false, length = 100) private String ward;
    @Column(nullable = false)               private String addressDetail;
    @Column(length = 500)                   private String note;
    private boolean isGift;
    @Column(length = 500)                   private String giftMessage;

    // ----- Tiền (server tự tính) -----
    @Column(nullable = false, precision = 18, scale = 2) private BigDecimal subtotal = BigDecimal.ZERO;
    @Column(nullable = false, precision = 18, scale = 2) private BigDecimal discountTotal = BigDecimal.ZERO;
    @Column(nullable = false, precision = 18, scale = 2) private BigDecimal customizationFeeTotal = BigDecimal.ZERO;
    @Column(nullable = false, precision = 18, scale = 2) private BigDecimal shippingFee = BigDecimal.ZERO;
    @Column(nullable = false, precision = 18, scale = 2) private BigDecimal totalAmount = BigDecimal.ZERO;

    // ----- Trạng thái -----
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod paymentMethod = PaymentMethod.MOMO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status = OrderStatus.RECEIVED;

    // ----- Kênh bán, đợt sản xuất, ưu đãi (cột mới phải cho phép null vì bảng có thể đã có dữ liệu) -----
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SalesChannel channel = SalesChannel.WEBSITE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")  // null = khách tự đặt trên web
    private AppUser createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")       // null = gửi ngay từ hàng có sẵn
    private SaleBatch batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id")     // voucher STILL US đã áp cho đơn này
    private Voucher voucher;

    // ----- Vận chuyển -----
    @Column(length = 50) private String shippingCarrier;
    @Column(length = 50) private String trackingNumber;

    private UUID cartId;                 // giỏ nguồn, không FK
    private Instant paidAt;
    private Instant completedAt;
    private Instant cancelledAt;
    @Column(length = 500) private String cancelReason;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<OrderStatusHistory> statusHistories = new ArrayList<>();
}