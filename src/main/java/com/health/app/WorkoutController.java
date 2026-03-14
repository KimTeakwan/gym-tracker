package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutRepository workoutRepository;

    // 1. 운동 기록 저장 (화면에서 폼 전송 시 사용)
    @PostMapping("/add")
    public RedirectView addWorkout(@ModelAttribute Workout workout) {
        workoutRepository.save(workout);
        // 저장 후 다시 대시보드 화면으로 튕겨줌! (실제 사용성 업그레이드)
        return new RedirectView("/dashboard");
    }

    // 2. 전체 데이터 조회 (API 전용)
    @GetMapping
    public List<Workout> getAll() {
        return workoutRepository.findAll();
    }

    // 3. 기록 삭제 기능 (실제로 쓸 때 필수!)
    @GetMapping("/delete/{id}")
    public RedirectView deleteWorkout(@PathVariable Long id) {
        workoutRepository.deleteById(id);
        return new RedirectView("/dashboard");
    }
}