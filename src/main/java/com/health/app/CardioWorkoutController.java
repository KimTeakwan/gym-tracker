package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/cardio")
@RequiredArgsConstructor
public class CardioWorkoutController {

    private final CardioWorkoutRepository repository;

    @PostMapping
    @ResponseBody
    public CardioWorkout save(@RequestBody CardioWorkout workout) {
        return repository.save(workout);
    }

    // 💡 [핵심 수정] @PathVariable 뒤에 ("id") 라는 이름표를 명시해 줬음!
    @GetMapping("/delete/{id}")
public String deleteCardio(@PathVariable("id") Long id) {
    repository.deleteById(id);
    return "redirect:/dashboard"; 
}
}