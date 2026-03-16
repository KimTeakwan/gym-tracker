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

    private String name;
    private String category;
    private int weight;
    private int reps;

    // 💡 [범인 검거] 'sets'는 예약어라 'workout_sets'로 이름을 바꿨음! [cite: 2026-02-19]
    @Column(name = "workout_sets")
    private int sets;

    @Column(name = "duration_minutes")
    private int durationMinutes;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }

    public int getTotalVolume() {
        return this.weight * this.reps * this.sets;
    }

    public int getCaloriesBurned() {
    double userWeight = 75.0; // 우리 친구 몸무게
    double met = 5.0;         // 웨이트 트레이닝 강도 
    
    // 1. 시간 기반 계산
    double caloriesByTime = met * userWeight * (this.durationMinutes / 60.0);
    
    // 2. 볼륨 기반 보너스 계산 (시간이 0일 때를 대비!) 
    // 1,000kg(1톤) 당 약 5kcal 소모한다고 가정
    double caloriesByVolume = (this.getTotalVolume() / 1000.0) * 5.0;
    
    // 두 값을 합산해서 더 정확한 소모량을 뽑아냄! 
    return (int) (caloriesByTime + caloriesByVolume);
}
}