package com.example.mmtbistore.model.customization;

import com.example.mmtbistore.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "icons")
public class Icon extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    // Không đặt tên là "group" vì GROUP là từ khoá SQL -> lỗi khi tạo bảng
    @Column(nullable = false, length = 50)
    private String groupName;            // "Trái tim", "Động vật"

    @Column(nullable = false, length = 500)
    private String url;                  // SVG

    private boolean isActive = true;
}
