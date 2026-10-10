package com.example.mmtbistore.model.order;

import com.example.mmtbistore.model.common.BaseEntity;
import com.example.mmtbistore.model.enums.OrderStatus;
import com.example.mmtbistore.model.user.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "order_status_histories")
public class OrderStatusHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private OrderStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus toStatus;

    @Column(length = 500)
    private String note;                 // "Đã bàn giao GHN"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by_id")  // null = hệ thống (IPN MoMo)
    private AppUser changedBy;
}
