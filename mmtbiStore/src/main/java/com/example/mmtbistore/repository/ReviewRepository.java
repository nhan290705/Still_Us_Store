package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.aftersales.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByOrderItem_Id(Long orderItemId);   // chặn đánh giá 2 lần

    // Review hiển thị ở trang sản phẩm, có phân trang
    Page<Review> findByProduct_IdAndIsVisibleTrueOrderByCreatedAtDesc(Long productId, Pageable pageable);
}
