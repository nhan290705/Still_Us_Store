package com.example.mmtbistore.service;

import com.example.mmtbistore.dto.OrderLookupResponse;
import com.example.mmtbistore.exception.BadRequestException;
import com.example.mmtbistore.exception.ResourceNotFoundException;
import com.example.mmtbistore.mapper.OrderMapper;
import com.example.mmtbistore.model.order.Order;
import com.example.mmtbistore.repository.OrderRepository;
import com.example.mmtbistore.util.PhoneNumbers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class OrderLookupService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional(readOnly = true)
    public OrderLookupResponse lookup(String phone, String code) {
        if (phone == null || phone.isBlank() || code == null || code.isBlank()) {
            throw new BadRequestException("Vui lòng nhập số điện thoại và mã đơn hàng");
        }

        String normalizedPhone = PhoneNumbers.normalize(phone);
        if (!PhoneNumbers.isValid(normalizedPhone)) {
            throw new BadRequestException("Số điện thoại không hợp lệ");
        }
        String normalizedCode = code.trim().toUpperCase(Locale.ROOT);

        // Sai SĐT hay sai mã đều báo CÙNG một lỗi, để người lạ không dò được mã đơn nào có thật
        Order order = orderRepository.findByOrderCodeAndCustomer_PhoneNumber(normalizedCode, normalizedPhone)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));

        return orderMapper.toLookup(order);
    }
}