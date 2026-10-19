package com.example.mmtbistore.service;

import com.example.mmtbistore.dto.StillUsRegisterRequest;
import com.example.mmtbistore.dto.StillUsResponse;
import com.example.mmtbistore.exception.BadRequestException;
import com.example.mmtbistore.exception.ConflictException;
import com.example.mmtbistore.exception.ResourceNotFoundException;
import com.example.mmtbistore.model.enums.OrderStatus;
import com.example.mmtbistore.model.enums.StillUsStatus;
import com.example.mmtbistore.model.memory.MemoryPage;
import com.example.mmtbistore.model.order.Order;
import com.example.mmtbistore.repository.MemoryPageRepository;
import com.example.mmtbistore.repository.OrderRepository;
import com.example.mmtbistore.util.PhoneNumbers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class StillUsService {

    private static final int CHAIN_DAYS = 365;

    // Mã QR: 10 ký tự, bỏ các chữ dễ nhầm (0/o, 1/l)
    private static final String SLUG_CHARS = "abcdefghijkmnpqrstuvwxyz23456789";
    private static final int SLUG_LENGTH = 10;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final OrderRepository orderRepository;
    private final MemoryPageRepository memoryPageRepository;

    @Transactional
    public StillUsResponse register(StillUsRegisterRequest request) {
        // 1. Chuẩn hoá đầu vào giống API tra cứu đơn
        String phone = PhoneNumbers.normalize(request.phone());
        if (!PhoneNumbers.isValid(phone)) {
            throw new BadRequestException("Số điện thoại không hợp lệ");
        }
        String orderCode = request.orderCode().trim().toUpperCase(Locale.ROOT);

        // 2. Đơn phải tồn tại và đúng SĐT người đặt (sai thì báo chung 404 như tra cứu đơn)
        Order order = orderRepository.findByOrderCodeAndCustomer_PhoneNumber(orderCode, phone)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));

        // 3. Đơn chưa huỷ, chưa hoàn
        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.RETURNED) {
            throw new BadRequestException("Đơn đã huỷ hoặc đã hoàn, không thể đăng ký STILL US");
        }

        // 4. Chỉ đơn có sản phẩm full box mới được đăng ký
        boolean hasCoupleBox = order.getItems().stream()
                .anyMatch(item -> Boolean.TRUE.equals(item.getProduct().getIsCoupleBox()));
        if (!hasCoupleBox) {
            throw new BadRequestException("Chỉ đơn có sản phẩm full box mới đăng ký được STILL US");
        }

        // 5. Trang kỷ niệm có thể đã được tạo sẵn lúc đặt đơn (để in QR lên hộp), hoặc chưa có
        MemoryPage page = memoryPageRepository.findByOrder_Id(order.getId()).orElse(null);
        if (page != null && page.getStartedAt() != null) {
            throw new ConflictException("Đơn này đã đăng ký STILL US");
        }

        // 6. Mỗi khách chỉ có một chuỗi đang chạy
        if (memoryPageRepository.existsByOrder_Customer_IdAndStatusAndStartedAtIsNotNull(
                order.getCustomer().getId(), StillUsStatus.ACTIVE)) {
            throw new ConflictException("Bạn đang có một chuỗi STILL US");
        }

        // 7. Chưa có trang thì tạo mới với mã QR ngẫu nhiên
        if (page == null) {
            page = new MemoryPage();
            page.setOrder(order);
            page.setSlug(newSlug());
        }
        page.setPartnerName1(request.ownerName().trim());
        page.setPartnerName2(request.partnerName().trim());
        page.setStartedAt(Instant.now());
        page.setStatus(StillUsStatus.ACTIVE);
        page.setActive(true);
        memoryPageRepository.save(page);

        return toResponse(page);
    }

    private StillUsResponse toResponse(MemoryPage page) {
        return new StillUsResponse(
                page.getSlug(),
                page.getPartnerName1(),
                page.getPartnerName2(),
                page.getStatus(),
                page.getStartedAt(),
                page.getStartedAt().plus(CHAIN_DAYS, ChronoUnit.DAYS)
        );
    }

    // Sinh mã ngẫu nhiên, thử lại nếu trùng (rất hiếm)
    private String newSlug() {
        for (int attempt = 0; attempt < 5; attempt++) {
            StringBuilder slug = new StringBuilder(SLUG_LENGTH);
            for (int i = 0; i < SLUG_LENGTH; i++) {
                slug.append(SLUG_CHARS.charAt(RANDOM.nextInt(SLUG_CHARS.length())));
            }
            if (!memoryPageRepository.existsBySlug(slug.toString())) {
                return slug.toString();
            }
        }
        throw new IllegalStateException("Không sinh được mã STILL US");
    }
}
