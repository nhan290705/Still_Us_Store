package com.example.mmtbistore.model.order;

import com.example.mmtbistore.model.common.BaseEntity;
import com.example.mmtbistore.model.enums.PaymentMethod;
import com.example.mmtbistore.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;

@Getter @Setter
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod method;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(nullable = false, unique = true, length = 50)
    private String requestId;            // unique mỗi lần gọi MoMo

    @Column(nullable = false, unique = true, length = 50)
    private String momoOrderId;          // OrderCode + "-" + lần thử

    @Column(length = 1000) private String payUrl;
    private Long transId;                // mã giao dịch MoMo
    private Integer resultCode;          // 0 = thành công
    @Column(length = 500) private String message;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String rawIpnPayload;        // lưu nguyên để đối soát

    private Instant paidAt;
}