package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam; // 💡 추가! 했음~
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WorkoutViewController {

    private final WorkoutRepository workoutRepository;
    private final CardioWorkoutRepository cardioWorkoutRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(value = "tab", required = false, defaultValue = "weight") String tab, // 💡 tab 파라미터 받기! [cite: 2026-03-15]
                            Model model, @AuthenticationPrincipal UserDetails userDetails) {
        
        Member currentMember = memberRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없음!"));

        model.addAttribute("nickname", currentMember.getNickname());
        model.addAttribute("activeTab", tab); // ✨ 현재 어떤 탭이 켜져야 하는지 알려줌!! [cite: 2026-03-15]

        List<Workout> myWorkouts = workoutRepository.findAllByMember(currentMember);
        List<Workout> validWorkouts = myWorkouts.stream()
                .filter(w -> w.getCreatedAt() != null && w.getCategory() != null)
                .collect(Collectors.toList());

        model.addAttribute("groupedWorkouts", validWorkouts.stream()
                .collect(Collectors.groupingBy(Workout::getCategory)));
        
        model.addAttribute("dailyVolumes", validWorkouts.stream()
                .collect(Collectors.groupingBy(
                    w -> w.getCreatedAt().toLocalDate().toString(),
                    Collectors.summingInt(Workout::getTotalVolume)
                )));

        model.addAttribute("cardioWorkouts", cardioWorkoutRepository.findAllByMember(currentMember));
        
        return "dashboard";
    }
}