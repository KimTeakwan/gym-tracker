package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class WorkoutViewController {

    private final WorkoutRepository workoutRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // 모든 운동 기록을 가져와서 화면에 뿌려줌!
        model.addAttribute("workouts", workoutRepository.findAll());
        return "dashboard"; // templates/dashboard.html 파일을 찾음
    }
}