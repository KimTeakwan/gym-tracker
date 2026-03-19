package com.health.app;

import jakarta.persistence.*;
import lombok.Data; // 👈 이거 추가!
import java.time.LocalDateTime;

@Entity
@Data // 👈 롬복이 자동으로 Getter, Setter를 다 만들어줌! 아주 똑똑한 녀석임~! [cite: 2026-03-15]
public class CardioWorkout {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private int durationSeconds;
    private double calories;
    private String intensity;
    private LocalDateTime createdAt = LocalDateTime.now();
}