package com.health.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class CardioWorkout {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;            // 운동 종목 (런닝머신 등)
    private String intensity;       // 강도 (중강도 등)
    private int durationSeconds;    // 시간 (초 단위)
    private double calories;        // 칼로리

    private LocalDateTime createdAt;

    @JsonIgnore 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 💡 회원별 연동을 위한 주인 이름표! 했음~ [cite: 2026-01-11]

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }
}