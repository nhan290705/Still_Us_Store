package com.example.mmtbistore.controller;

import com.example.mmtbistore.dto.StillUsRegisterRequest;
import com.example.mmtbistore.dto.StillUsResponse;
import com.example.mmtbistore.service.StillUsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
}
