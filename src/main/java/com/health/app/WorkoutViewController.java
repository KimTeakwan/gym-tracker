package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WorkoutViewController {

    private final WorkoutRepository workoutRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Workout> allWorkouts = workoutRepository.findAll();
        
        // 1. 부위별 그룹화
        Map<String, List<Workout>> groupedWorkouts = allWorkouts.stream()
                .collect(Collectors.groupingBy(Workout::getCategory));
        
        // 2. 그래프용 날짜별 볼륨 계산
        Map<String, Integer> dailyVolumes = allWorkouts.stream()
                .collect(Collectors.groupingBy(
                    w -> w.getCreatedAt().toLocalDate().toString(),
                    Collectors.summingInt(Workout::getTotalVolume)
                ));

        // 3. [신규] 오늘 하루 총 볼륨 계산! [cite: 2026-01-11, 2026-02-19]
        LocalDate today = LocalDate.now();
        int todayTotalVolume = allWorkouts.stream()
                .filter(w -> w.getCreatedAt().toLocalDate().isEqual(today))
                .mapToInt(Workout::getTotalVolume)
                .sum();
                
        model.addAttribute("groupedWorkouts", groupedWorkouts);
        model.addAttribute("dailyVolumes", dailyVolumes);
        model.addAttribute("todayTotalVolume", todayTotalVolume); // 오늘 총 볼륨 전달!
        
        return "dashboard";
    }
}