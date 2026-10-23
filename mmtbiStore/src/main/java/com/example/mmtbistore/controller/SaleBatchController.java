package com.example.mmtbistore.controller;

import com.example.mmtbistore.dto.SaleBatchRequest;
import com.example.mmtbistore.dto.SaleBatchResponse;
import com.example.mmtbistore.service.SaleBatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API quản lý đợt bán (admin).
 *
 * GET    /api/admin/batches          – Danh sách tất cả đợt
 * GET    /api/admin/batches/{id}     – Chi tiết 1 đợt
 * POST   /api/admin/batches          – Tạo đợt mới (COLLECTING)
 * PUT    /api/admin/batches/{id}     – Sửa tên / ghi chú
 * PATCH  /api/admin/batches/{id}/advance – Chuyển trạng thái tiếp theo
 */
@RestController
@RequestMapping("/api/admin/batches")
@RequiredArgsConstructor
public class SaleBatchController {

    private final SaleBatchService saleBatchService;

    @GetMapping
    public List<SaleBatchResponse> getAllBatches() {
        return saleBatchService.getAllBatches();
    }

    @GetMapping("/{id}")
    public SaleBatchResponse getBatch(@PathVariable("id") Long id) {
        return saleBatchService.getBatch(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleBatchResponse createBatch(@Valid @RequestBody SaleBatchRequest request) {
        return saleBatchService.createBatch(request);
    }

    @PutMapping("/{id}")
    public SaleBatchResponse updateBatch(@PathVariable("id") Long id,
                                         @Valid @RequestBody SaleBatchRequest request) {
        return saleBatchService.updateBatch(id, request);
    }

    /** Chuyển trạng thái: COLLECTING → PRODUCING → COMPLETED */
    @PatchMapping("/{id}/advance")
    public SaleBatchResponse advanceStatus(@PathVariable("id") Long id) {
        return saleBatchService.advanceStatus(id);
    }
}
