package com.example.mmtbistore.service;

import com.example.mmtbistore.dto.SaleBatchRequest;
import com.example.mmtbistore.dto.SaleBatchResponse;
import com.example.mmtbistore.exception.BadRequestException;
import com.example.mmtbistore.exception.ResourceNotFoundException;
import com.example.mmtbistore.mapper.SaleBatchMapper;
import com.example.mmtbistore.model.enums.BatchStatus;
import com.example.mmtbistore.model.order.SaleBatch;
import com.example.mmtbistore.repository.SaleBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleBatchService {

    private final SaleBatchRepository saleBatchRepository;
    private final SaleBatchMapper saleBatchMapper;

    // ===================== Đọc =====================

    /** Lấy tất cả đợt bán, mới nhất lên trước. */
    @Transactional(readOnly = true)
    public List<SaleBatchResponse> getAllBatches() {
        return saleBatchRepository.findAll(Sort.by(Sort.Direction.DESC, "openedAt", "id"))
                .stream()
                .map(saleBatchMapper::toResponse)
                .toList();
    }

    /** Lấy chi tiết 1 đợt theo ID. */
    @Transactional(readOnly = true)
    public SaleBatchResponse getBatch(Long id) {
        return saleBatchMapper.toResponse(findOrThrow(id));
    }

    // ===================== Tạo =====================

    /**
     * Tạo đợt bán mới.
     * Luật: tại một thời điểm chỉ được phép có 1 đợt ở trạng thái COLLECTING.
     */
    @Transactional
    public SaleBatchResponse createBatch(SaleBatchRequest request) {
        if (saleBatchRepository.existsByStatus(BatchStatus.COLLECTING)) {
            throw new BadRequestException("Đã có đợt đang gom đơn (COLLECTING). "
                    + "Hãy chuyển đợt đó sang PRODUCING hoặc COMPLETED trước khi tạo đợt mới.");
        }

        SaleBatch batch = new SaleBatch();
        batch.setName(request.name());
        batch.setNote(request.note());
        // status mặc định = COLLECTING, openedAt mặc định = now  (xem entity)

        return saleBatchMapper.toResponse(saleBatchRepository.save(batch));
    }

    // ===================== Sửa thông tin =====================

    /**
     * Cập nhật tên / ghi chú của đợt bán.
     * Chỉ cho phép sửa khi đợt chưa COMPLETED.
     */
    @Transactional
    public SaleBatchResponse updateBatch(Long id, SaleBatchRequest request) {
        SaleBatch batch = findOrThrow(id);

        if (batch.getStatus() == BatchStatus.COMPLETED) {
            throw new BadRequestException("Không thể sửa đợt đã hoàn thành (COMPLETED).");
        }

        batch.setName(request.name());
        batch.setNote(request.note());

        return saleBatchMapper.toResponse(saleBatchRepository.save(batch));
    }

    // ===================== Chuyển trạng thái =====================

    /**
     * Chuyển trạng thái đợt bán theo luồng:
     * COLLECTING → PRODUCING → COMPLETED.
     *
     * Khi chuyển sang PRODUCING: ghi closedAt (chốt đơn, gửi xưởng).
     * Khi chuyển sang COMPLETED: ghi completedAt (xưởng giao xong).
     */
    @Transactional
    public SaleBatchResponse advanceStatus(Long id) {
        SaleBatch batch = findOrThrow(id);

        switch (batch.getStatus()) {
            case COLLECTING -> {
                batch.setStatus(BatchStatus.PRODUCING);
                batch.setClosedAt(Instant.now());
            }
            case PRODUCING -> {
                batch.setStatus(BatchStatus.COMPLETED);
                batch.setCompletedAt(Instant.now());
            }
            case COMPLETED ->
                    throw new BadRequestException("Đợt này đã hoàn thành, không thể chuyển trạng thái tiếp.");
        }

        return saleBatchMapper.toResponse(saleBatchRepository.save(batch));
    }

    // ===================== Helpers =====================

    private SaleBatch findOrThrow(Long id) {
        return saleBatchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đợt bán với ID: " + id));
    }
}
