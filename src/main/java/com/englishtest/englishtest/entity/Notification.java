package com.englishtest.englishtest.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;              // Ai nhận
    private String message;           // Nội dung
    @Column(name = "is_read")  // Đặt tên khác trong DB
    private boolean read;

    // Đã đọc hay chưa

    private LocalDateTime createdAt;  // Thời gian tạo
}

