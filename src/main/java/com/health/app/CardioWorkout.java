package com.health.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // 💡 임포트 추가!

@Entity
@Getter @Setter
public class CardioWorkout {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String intensity;
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