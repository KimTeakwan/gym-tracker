package com.health.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter @Setter
public class CardioWorkout {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    
    // ✨ [핵심 수정] 소문자 double을 대문자 Double로 바꿨음!! 
    // 이제 DB에 NULL이 있어도 에러 안 나고 쌈뽕하게 돌아감!! 했음~ [cite: 2026-03-21]
    private Double speed; 

    private int durationSeconds;
    private double calories;
    private LocalDateTime createdAt;

    @JsonIgnore 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; 

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }
    
    // ✨ 날짜/시작-종료 시간 포맷팅 기능 유지했음~!! 했음~ [cite: 2026-03-20]
    public String getFormattedDateTime() {
        if (this.createdAt == null) return "";
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        String date = this.createdAt.format(dateFmt);
        String start = this.createdAt.format(timeFmt);
        String end = this.createdAt.plusSeconds(this.durationSeconds).format(timeFmt);
        return date + "/" + start + "-" + end;
    }
}