package com.health.app;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CardioWorkoutRepository extends JpaRepository<CardioWorkout, Long> {
    // ✨ [신규 추가] 특정 회원(Member)의 유산소 기록만 필터링!
    List<CardioWorkout> findAllByMember(Member member);
}