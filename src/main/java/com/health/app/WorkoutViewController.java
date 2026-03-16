package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WorkoutViewController {

    private final WorkoutRepository workoutRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // 1. 모든 기록을 가져오되, 리스트가 비어있어도 에러 안 나게 처리! [cite: 2026-02-19]
        List<Workout> allWorkouts = Optional.ofNullable(workoutRepository.findAll()).orElse(new ArrayList<>());
        
        // 2. 날짜(createdAt)가 없는 데이터는 제외하고 계산 (NullPointerException 방지) [cite: 2026-02-19]
        List<Workout> validWorkouts = allWorkouts.stream()
                .filter(w -> w.getCreatedAt() != null && w.getCategory() != null)
                .collect(Collectors.toList());

        // 3. 부위별 그룹화
        Map<String, List<Workout>> groupedWorkouts = validWorkouts.stream()
                .collect(Collectors.groupingBy(Workout::getCategory));
        
        // 4. 그래프용 날짜별 볼륨 계산
        Map<String, Integer> dailyVolumes = validWorkouts.stream()
                .collect(Collectors.groupingBy(
                    w -> w.getCreatedAt().toLocalDate().toString(),
                    Collectors.summingInt(Workout::getTotalVolume)
                ));

        // 5. 오늘 총 볼륨 (안전하게 계산)
        LocalDate today = LocalDate.now();
        int todayTotalVolume = validWorkouts.stream()
                .filter(w -> w.getCreatedAt().toLocalDate().isEqual(today))
                .mapToInt(Workout::getTotalVolume)
                .sum();
                
        model.addAttribute("groupedWorkouts", groupedWorkouts);
        model.addAttribute("dailyVolumes", dailyVolumes);
        model.addAttribute("todayTotalVolume", todayTotalVolume);
        
        return "dashboard";
    }
}