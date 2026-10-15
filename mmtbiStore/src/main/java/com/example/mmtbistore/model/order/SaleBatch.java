package com.example.mmtbistore.model.order;

import com.example.mmtbistore.model.common.BaseEntity;
import com.example.mmtbistore.model.enums.BatchStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

// Đợt sản xuất: gom đơn của những biến thể hết hàng có sẵn để đặt xưởng.
// Nút Mua chỉ xám khi biến thể hết hàng có sẵn VÀ không có đợt nào đang COLLECTING.
@Getter @Setter
@Entity
@Table(name = "sale_batches")
public class SaleBatch extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;                 // "Đợt 1"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BatchStatus status = BatchStatus.COLLECTING;

    @Column(nullable = false)
    private Instant openedAt = Instant.now();
    private Instant closedAt;            // chốt đơn, gửi xưởng
    private Instant completedAt;         // xưởng giao xong

    @Column(length = 500)
    private String note;
}
