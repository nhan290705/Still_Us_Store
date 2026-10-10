package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.customization.UploadedAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UploadedAssetRepository extends JpaRepository<UploadedAsset, UUID> {
    // Job dọn file rác: file tạm upload quá X ngày mà không gắn vào đơn
    List<UploadedAsset> findByIsTemporaryTrueAndCreatedAtBefore(Instant time);
}
