package com.example.mmtbistore.service;

import com.example.mmtbistore.dto.StillUsCancelRequest;
import com.example.mmtbistore.dto.StillUsCancelResponse;
import com.example.mmtbistore.dto.StillUsDetailResponse;
import com.example.mmtbistore.dto.StillUsRegisterRequest;
import com.example.mmtbistore.dto.StillUsResponse;
import com.example.mmtbistore.exception.BadRequestException;
import com.example.mmtbistore.exception.ConflictException;
import com.example.mmtbistore.exception.ForbiddenException;
import com.example.mmtbistore.exception.ResourceNotFoundException;
import com.example.mmtbistore.model.enums.OrderStatus;
import com.example.mmtbistore.model.enums.StillUsStatus;
import com.example.mmtbistore.model.enums.VoucherStatus;
import com.example.mmtbistore.model.memory.MemoryPage;
import com.example.mmtbistore.model.memory.Voucher;
import com.example.mmtbistore.model.order.Order;
import com.example.mmtbistore.repository.MemoryPageRepository;
import com.example.mmtbistore.repository.OrderRepository;
import com.example.mmtbistore.repository.VoucherRepository;
import com.example.mmtbistore.util.PhoneNumbers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class StillUsService {

    private static final int CHAIN_DAYS = 365;
    private static final int[] MILESTONE_DAYS = {90, 180, 365};

    // Mã QR: 10 ký tự, bỏ các chữ dễ nhầm (0/o, 1/l)
    private static final String SLUG_CHARS = "abcdefghijkmnpqrstuvwxyz23456789";
    private static final int SLUG_LENGTH = 10;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final OrderRepository orderRepository;
    private final MemoryPageRepository memoryPageRepository;
    private final VoucherRepository voucherRepository;

    // ===================== Mục 6: đăng ký =====================

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

    // ===================== Mục 7: xem chuỗi khi quét QR =====================

    @Transactional(readOnly = true)
    public StillUsDetailResponse getByCode(String code) {
        MemoryPage page = findPage(code);

        // QR in sẵn trên hộp nhưng khách chưa đăng ký: chỉ báo chưa đăng ký, không lộ gì thêm
        if (page.getStartedAt() == null) {
            return new StillUsDetailResponse(page.getSlug(), false, null, null, null,
                    null, null, null, 0, 0, page.getThemeKey(), List.of());
        }

        Instant now = Instant.now();
        Instant startedAt = page.getStartedAt();
        Instant endsAt = endsAt(page);
        StillUsStatus status = currentStatus(page, now);

        // Chuỗi đã huỷ thì các mốc tính đến lúc huỷ; còn lại tính đến bây giờ
        Instant reference = status == StillUsStatus.CANCELLED && page.getCancelledAt() != null
                ? page.getCancelledAt() : now;
        List<StillUsDetailResponse.Milestone> milestones = new ArrayList<>();
        for (int day : MILESTONE_DAYS) {
            Instant date = startedAt.plus(day, ChronoUnit.DAYS);
            milestones.add(new StillUsDetailResponse.Milestone(day, date, !reference.isBefore(date)));
        }

        long daysLeft = 0;
        if (status == StillUsStatus.ACTIVE) {
            long secondsLeft = Duration.between(now, endsAt).getSeconds();
            daysLeft = (secondsLeft + 86_399) / 86_400;   // làm tròn lên: còn 1 giây vẫn tính 1 ngày
        }

        // Chuỗi thứ mấy của khách (năm thứ 2, 3... để đổi giao diện)
        long yearNumber = memoryPageRepository.countByOrder_Customer_IdAndStartedAtLessThanEqual(
                page.getOrder().getCustomer().getId(), startedAt);

        return new StillUsDetailResponse(
                page.getSlug(),
                true,
                page.getPartnerName1(),
                page.getPartnerName2(),
                status,
                startedAt,
                endsAt,
                page.getCancelledAt(),
                daysLeft,
                yearNumber,
                page.getThemeKey(),
                milestones
        );
    }

    // ===================== Mục 8: huỷ chuỗi =====================

    @Transactional
    public StillUsCancelResponse cancel(String code, StillUsCancelRequest request) {
        MemoryPage page = findPage(code);
        if (page.getStartedAt() == null) {
            throw new ResourceNotFoundException("Không tìm thấy chuỗi STILL US");
        }

        // SĐT phải khớp người đặt đơn, để người lạ quét QR không huỷ được
        String phone = PhoneNumbers.normalize(request.phone());
        if (!PhoneNumbers.isValid(phone)) {
            throw new BadRequestException("Số điện thoại không hợp lệ");
        }
        if (!phone.equals(page.getOrder().getCustomer().getPhoneNumber())) {
            throw new ForbiddenException("Số điện thoại không khớp với chuỗi STILL US này");
        }

        Instant now = Instant.now();
        StillUsStatus status = currentStatus(page, now);
        if (status == StillUsStatus.CANCELLED) {
            throw new ConflictException("Chuỗi STILL US này đã huỷ trước đó");
        }
        if (status == StillUsStatus.COMPLETED) {
            throw new ConflictException("Chuỗi STILL US đã hoàn thành, không thể huỷ");
        }

        page.setStatus(StillUsStatus.CANCELLED);
        page.setCancelledAt(now);
        page.setCancelReason(request.reason() == null || request.reason().isBlank() ? null : request.reason().trim());

        // Huỷ chỉ mất ưu đãi: voucher còn dùng được chuyển REVOKED, khách vẫn mua bình thường
        List<Voucher> activeVouchers = voucherRepository.findByMemoryPage_IdAndStatus(page.getId(), VoucherStatus.ACTIVE);
        activeVouchers.forEach(voucher -> voucher.setStatus(VoucherStatus.REVOKED));

        return new StillUsCancelResponse(page.getSlug(), page.getStatus(), page.getCancelledAt(), activeVouchers.size());
    }

    // ===================== Hàm phụ =====================

    private MemoryPage findPage(String code) {
        String slug = code == null ? "" : code.trim().toLowerCase(Locale.ROOT);
        return memoryPageRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuỗi STILL US"));
    }

    private Instant endsAt(MemoryPage page) {
        return page.getStartedAt().plus(CHAIN_DAYS, ChronoUnit.DAYS);
    }

    // Đủ 365 ngày mà chưa huỷ thì coi như đã hoàn thành (chưa cần job cập nhật database)
    private StillUsStatus currentStatus(MemoryPage page, Instant now) {
        if (page.getStatus() == StillUsStatus.ACTIVE && !now.isBefore(endsAt(page))) {
            return StillUsStatus.COMPLETED;
        }
        return page.getStatus();
    }

    private StillUsResponse toResponse(MemoryPage page) {
        return new StillUsResponse(
                page.getSlug(),
                page.getPartnerName1(),
                page.getPartnerName2(),
                page.getStatus(),
                page.getStartedAt(),
                endsAt(page)
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
