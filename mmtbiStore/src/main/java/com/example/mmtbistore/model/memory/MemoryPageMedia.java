package com.example.mmtbistore.model.memory;

import com.example.mmtbistore.model.common.BaseEntity;
import com.example.mmtbistore.model.enums.MediaType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@Entity
@Table(name = "memory_page_media")
public class MemoryPageMedia extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "memory_page_id", nullable = false)
    private MemoryPage memoryPage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MediaType type;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(length = 300)
    private String caption;

    private LocalDate takenAt;           // có ngày -> hiển thị timeline
    private int sortOrder;
}
