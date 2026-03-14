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

    @Column(name = "workout_sets") // MySQL 예약어 피하기! [cite: 2026-02-19]
    private int sets;         
    
    private LocalDateTime createdAt; 
    private String memo;             

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public int getTotalVolume() {
        return this.weight * this.reps * this.sets;
    }
}