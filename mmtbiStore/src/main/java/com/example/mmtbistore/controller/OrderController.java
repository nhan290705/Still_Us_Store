package com.example.mmtbistore.controller;

import com.example.mmtbistore.dto.OrderLookupResponse;
import com.example.mmtbistore.service.OrderLookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderLookupService orderLookupService;

    // GET /api/orders/lookup?phone=0901234567&code=DD261015-A1B2C
    // required = false: để service tự báo lỗi 400 bằng tiếng Việt khi thiếu tham số
    @GetMapping("/lookup")
    public OrderLookupResponse lookup(
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "code", required = false) String code) {
        return orderLookupService.lookup(phone, code);
    }
}
