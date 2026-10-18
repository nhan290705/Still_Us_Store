package com.example.mmtbistore.service;

import com.example.mmtbistore.dto.OrderLookupResponse;
import com.example.mmtbistore.exception.BadRequestException;
import com.example.mmtbistore.exception.ResourceNotFoundException;
import com.example.mmtbistore.mapper.OrderMapper;
import com.example.mmtbistore.model.order.Order;
import com.example.mmtbistore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class OrderLookupService {

    // SĐT Việt Nam sau khi chuẩn hoá: 10 số, bắt đầu bằng 0
    private static final String PHONE_PATTERN = "0\\d{9}";

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional(readOnly = true)
    public OrderLookupResponse lookup(String phone, String code) {
        if (phone == null || phone.isBlank() || code == null || code.isBlank()) {
            throw new BadRequestException("Vui lòng nhập số điện thoại và mã đơn hàng");
        }

        String normalizedPhone = normalizePhone(phone);
        if (!normalizedPhone.matches(PHONE_PATTERN)) {
            throw new BadRequestException("Số điện thoại không hợp lệ");
        }
        String normalizedCode = code.trim().toUpperCase(Locale.ROOT);

        // Sai SĐT hay sai mã đều báo CÙNG một lỗi, để người lạ không dò được mã đơn nào có thật
        Order order = orderRepository.findByOrderCodeAndCustomer_PhoneNumber(normalizedCode, normalizedPhone)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));

        return orderMapper.toLookup(order);
    }

    // "090 123 4567", "090.123.4567", "+84901234567" -> "0901234567"
    private String normalizePhone(String phone) {
        String digits = phone.replaceAll("[\\s.\\-]", "");
        if (digits.startsWith("+84")) {
            digits = "0" + digits.substring(3);
        } else if (digits.startsWith("84") && digits.length() == 11) {
            digits = "0" + digits.substring(2);
        }
        return digits;
    }
}
