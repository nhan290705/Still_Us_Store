package com.example.mmtbistore.model.aftersales;

import com.example.mmtbistore.model.catalog.Product;
import com.example.mmtbistore.model.common.BaseEntity;
import com.example.mmtbistore.model.order.OrderItem;
import com.example.mmtbistore.model.user.AppUser;
import com.example.mmtbistore.model.user.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {

    // 1-1: mỗi món trong đơn chỉ được đánh giá 1 lần
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_item_id", nullable = false, unique = true)
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Min(1) @Max(5)
    private int rating;

    @Column(columnDefinition = "text")
    private String comment;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    private List<String> imageUrls = new ArrayList<>();

    private boolean isVisible = true;

    @Column(columnDefinition = "text")
    private String reply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replied_by_id")
    private AppUser repliedBy;

    private Instant repliedAt;
}
