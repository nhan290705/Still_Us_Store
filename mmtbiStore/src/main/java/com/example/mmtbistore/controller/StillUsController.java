package com.example.mmtbistore.controller;

import com.example.mmtbistore.dto.StillUsCancelRequest;
import com.example.mmtbistore.dto.StillUsCancelResponse;
import com.example.mmtbistore.dto.StillUsDetailResponse;
import com.example.mmtbistore.dto.StillUsRegisterRequest;
import com.example.mmtbistore.dto.StillUsResponse;
import com.example.mmtbistore.service.StillUsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/still-us")
@RequiredArgsConstructor
public class StillUsController {

    private final StillUsService stillUsService;

    // POST /api/still-us  body: {"orderCode":"...","phone":"...","ownerName":"...","partnerName":"..."}
    // @Valid: kiểm tra các chú thích @NotBlank, @Size trong StillUsRegisterRequest trước khi vào service
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StillUsResponse register(@Valid @RequestBody StillUsRegisterRequest request) {
        return stillUsService.register(request);
    }

    // GET /api/still-us/{code}: trang công khai khi quét QR, không có SĐT hay thông tin đơn
    @GetMapping("/{code}")
    public StillUsDetailResponse getByCode(@PathVariable("code") String code) {
        return stillUsService.getByCode(code);
    }

    // POST /api/still-us/{code}/cancel  body: {"phone":"...","reason":"..."}
    @PostMapping("/{code}/cancel")
    public StillUsCancelResponse cancel(@PathVariable("code") String code,
                                        @Valid @RequestBody StillUsCancelRequest request) {
        return stillUsService.cancel(code, request);
    }
}
