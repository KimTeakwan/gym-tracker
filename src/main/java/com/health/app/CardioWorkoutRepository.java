package com.health.app;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CardioWorkoutRepository extends JpaRepository<CardioWorkout, Long> {
}