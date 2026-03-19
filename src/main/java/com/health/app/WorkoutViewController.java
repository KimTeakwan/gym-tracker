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
    // 💡 [수정 1] 유산소 DB 저장소도 무조건 주입받아야 함!
    private final CardioWorkoutRepository cardioWorkoutRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Workout> allWorkouts = Optional.ofNullable(workoutRepository.findAll()).orElse(new ArrayList<>());
        
        List<Workout> validWorkouts = allWorkouts.stream()
                .filter(w -> w.getCreatedAt() != null && w.getCategory() != null)
                .collect(Collectors.toList());

        Map<String, List<Workout>> groupedWorkouts = validWorkouts.stream()
                .collect(Collectors.groupingBy(Workout::getCategory));
        
        Map<String, Integer> dailyVolumes = validWorkouts.stream()
                .collect(Collectors.groupingBy(
                    w -> w.getCreatedAt().toLocalDate().toString(),
                    Collectors.summingInt(Workout::getTotalVolume)
                ));

        LocalDate today = LocalDate.now();
        int todayTotalVolume = validWorkouts.stream()
                .filter(w -> w.getCreatedAt().toLocalDate().isEqual(today))
                .mapToInt(Workout::getTotalVolume)
                .sum();

        // 💡 [수정 2] 대문자 CardioWorkoutRepository가 아니라 소문자 변수명으로 써야 함!
        List<CardioWorkout> cardioWorkouts = cardioWorkoutRepository.findAll();

        model.addAttribute("cardioWorkouts", cardioWorkouts);        
        // 💡 [수정 3] goupedWorkouts -> groupedWorkouts 오타 수정!
        model.addAttribute("groupedWorkouts", groupedWorkouts);
        model.addAttribute("dailyVolumes", dailyVolumes);
        model.addAttribute("todayTotalVolume", todayTotalVolume);
        
        return "dashboard";
    }
}