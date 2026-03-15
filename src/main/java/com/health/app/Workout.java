package com.health.app;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import jakarta.validation.constraints.Min;

@Entity
@Getter @Setter
public class Workout {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;      
    private String category;  

    @Min(0) // 무게는 당연히 0 이상! [cite: 2026-02-19]
    private int weight;   

    @Min(0) // 횟수도 음수면 근손실 남~ [cite: 2026-02-19]
    private int reps;         

    @Column(name = "workout_sets")
    @Min(0) // 세트 수도 철벽 수비! [cite: 2026-02-19]
    private int sets;         
    
    private LocalDateTime createdAt; 
    private String memo;             

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // 총 볼륨 계산 (무게 * 횟수 * 세트)
    public int getTotalVolume() {
        return this.weight * this.reps * this.sets;
    }
}