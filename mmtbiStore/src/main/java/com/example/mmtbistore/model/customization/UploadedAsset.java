package com.example.mmtbistore.model.customization;

import com.example.mmtbistore.model.enums.AssetPurpose;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter
@Entity
@Table(name = "uploaded_assets",
        indexes = @Index(name = "ix_assets_cleanup", columnList = "is_temporary, created_at"))
public class UploadedAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(nullable = false, length = 300)
    private String storageKey;           // key trên Cloudinary/S3 để xoá file

    @Column(nullable = false, length = 100)
    private String contentType;

    private long sizeBytes;
    private Integer width;
    private Integer height;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AssetPurpose purpose;

    private UUID cartId;                 // chỉ lưu giá trị, không FK
    private Long orderId;                // chỉ lưu giá trị, không FK

    private boolean isTemporary = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
