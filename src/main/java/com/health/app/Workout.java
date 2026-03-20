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

    // ✅ 기존 기능 유지: 'sets' 예약어 회피 [cite: 2026-03-15]
    @Column(name = "workout_sets")
    private int sets;

    @Column(name = "duration_minutes")
    private int durationMinutes;

    private LocalDateTime createdAt;

    // ✨ [신규 추가] 이 기록의 주인(Member) 연결! (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; 

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }

    // ✅ 기존 기능 유지: 총 볼륨 계산 [cite: 2026-03-15]
    public int getTotalVolume() {
        return this.weight * this.reps * this.sets;
    }

    // ✅ 기존 기능 유지: 디테일한 칼로리 계산 로직 (75kg, MET 5.0 적용)
    public int getCaloriesBurned() {
        double userWeight = 75.0; 
        double met = 5.0;         
        double caloriesByTime = met * userWeight * (this.durationMinutes / 60.0);
        double caloriesByVolume = (this.getTotalVolume() / 1000.0) * 5.0;
        return (int) (caloriesByTime + caloriesByVolume);
    }

    // ✨ [신규 추가] 웨이트 기록용 시간 변환기!! 했음~ 
    public String getFormattedDateTime() {
        if (this.createdAt == null) return "";
        java.time.format.DateTimeFormatter dateFmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
        java.time.format.DateTimeFormatter timeFmt = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
        
        String date = this.createdAt.format(dateFmt);
        String start = this.createdAt.format(timeFmt);
        String end = this.createdAt.plusMinutes(this.durationMinutes).format(timeFmt);
        
        return date + "/" + start + "-" + end;
    }
}