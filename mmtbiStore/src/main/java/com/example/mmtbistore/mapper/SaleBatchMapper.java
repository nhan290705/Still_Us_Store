package com.example.mmtbistore.mapper;

import com.example.mmtbistore.dto.SaleBatchResponse;
import com.example.mmtbistore.model.order.SaleBatch;
import org.springframework.stereotype.Component;

@Component
public class SaleBatchMapper {

    public SaleBatchResponse toResponse(SaleBatch batch) {
        return new SaleBatchResponse(
                batch.getId(),
                batch.getName(),
                batch.getStatus(),
                batch.getOpenedAt(),
                batch.getClosedAt(),
                batch.getCompletedAt(),
                batch.getNote(),
                batch.getCreatedAt(),
                batch.getUpdatedAt()
        );
    }
}
