package com.example.mmtbistore.model.aftersales;

import com.example.mmtbistore.model.common.BaseEntity;
import com.example.mmtbistore.model.enums.ReturnReason;
import com.example.mmtbistore.model.enums.ReturnStatus;
import com.example.mmtbistore.model.enums.ReturnType;
import com.example.mmtbistore.model.order.Order;
import com.example.mmtbistore.model.user.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "return_requests")
public class ReturnRequest extends BaseEntity {

    @Column(nullable = false, unique = true, length = 30)
    private String requestCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReturnType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReturnReason reason;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    private List<String> evidenceImageUrls = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReturnStatus status = ReturnStatus.PENDING;

    @Column(precision = 18, scale = 2)
    private BigDecimal refundAmount;

    @Column(length = 1000)
    private String staffNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handled_by_id")
    private AppUser handledBy;

    private Instant resolvedAt;

    @OneToMany(mappedBy = "returnRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReturnRequestItem> items = new ArrayList<>();
}