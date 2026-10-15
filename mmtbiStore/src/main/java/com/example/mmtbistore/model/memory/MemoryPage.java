package com.example.mmtbistore.model.memory;

import com.example.mmtbistore.model.common.BaseEntity;
import com.example.mmtbistore.model.enums.MemoryVisibility;
import com.example.mmtbistore.model.enums.StillUsStatus;
import com.example.mmtbistore.model.order.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "memory_pages")
public class MemoryPage extends BaseEntity {

    // 1-1 với đơn hàng
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(nullable = false, unique = true, length = 20)
    private String slug;                 // ngẫu nhiên, QR trỏ tới /m/{slug}

    @Column(length = 200) private String title;
    @Column(length = 100) private String partnerName1;
    @Column(length = 100) private String partnerName2;
    private LocalDate anniversaryDate;

    @Column(columnDefinition = "text")
    private String story;

    @Column(length = 500) private String coverImageUrl;
    @Column(length = 500) private String musicUrl;

    @Column(nullable = false, length = 50)
    private String themeKey = "default";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemoryVisibility visibility = MemoryVisibility.PUBLIC;

    private String pinHash;
    private boolean isActive;            // bật khi đơn đã thanh toán
    private int viewCount;

    // ----- Chuỗi STILL US 365 ngày (chỉ đơn có sản phẩm full box) -----
    private Instant startedAt;           // lúc khách gửi thông tin, bắt đầu đếm 365 ngày
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StillUsStatus status = StillUsStatus.ACTIVE;
    private Instant cancelledAt;         // huỷ = chỉ mất ưu đãi, vẫn mua hàng bình thường
    // Đặt tên cột rõ ràng: để mặc định Spring đặt thành milestone90sent_at
    @Column(name = "milestone_90_sent_at")
    private Instant milestone90SentAt;
    @Column(name = "milestone_180_sent_at")
    private Instant milestone180SentAt;
    @Column(name = "milestone_365_sent_at")
    private Instant milestone365SentAt;

    @OneToMany(mappedBy = "memoryPage", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<MemoryPageMedia> media = new ArrayList<>();
}
