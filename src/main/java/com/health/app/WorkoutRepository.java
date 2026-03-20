package com.health.app;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    // ✨ [신규 추가] 로그인한 회원(Member)의 기록만 필터링해서 가져오기!
    List<Workout> findAllByMember(Member member);
}