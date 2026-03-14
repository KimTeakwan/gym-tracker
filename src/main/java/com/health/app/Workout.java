package com.health.app;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Workout {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;      // 운동 이름 (ex: 벤치프레스)
    private String category;  // 부위 (가슴, 등, 하체, 어깨, 팔)
    private int weight;       // 무게 (kg)
    private int reps;         // 횟수
    private int sets;         // 세트 수
    
    private LocalDateTime createdAt; // 운동한 날짜와 시간
    private String memo;             // 컨디션이나 특이사항

    // 데이터 저장 전 자동으로 날짜 입력!
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // 총 볼륨 계산 (무게 * 횟수 * 세트)
    public int getTotalVolume() {
        return this.weight * this.reps * this.sets;
    }
}