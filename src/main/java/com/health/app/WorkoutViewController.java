package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final CardioWorkoutRepository cardioWorkoutRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        
        // 1. 💡 [기존 유지] 로그인한 사람의 정보 가져오기 [cite: 2026-03-15]
        Member currentMember = memberRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없음!"));

        // 2. 💡 [기존 유지] 닉네임 화면에 배달하기 [cite: 2026-03-15]
        model.addAttribute("nickname", currentMember.getNickname());

        // 3. 💡 [기존 유지] 내 웨이트 기록만 가져오기 [cite: 2026-03-15]
        List<Workout> myWorkouts = workoutRepository.findAllByMember(currentMember);
        
        // --- 통계 계산 로직 (기존 코드 그대로 유지함!) [cite: 2026-03-20] ---
        List<Workout> validWorkouts = myWorkouts.stream()
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

        // ✨ [수정 완료] 이제 유산소도 '전체'가 아니라 '내 기록'만 가져옴!! [cite: 2026-03-15]
        // findAll() ➡️ findAllByMember(currentMember) 로 변경 완료! 했음~ [cite: 2026-01-11]
        List<CardioWorkout> myCardioWorkouts = cardioWorkoutRepository.findAllByMember(currentMember);

        model.addAttribute("cardioWorkouts", myCardioWorkouts);        
        model.addAttribute("groupedWorkouts", groupedWorkouts); 
        model.addAttribute("dailyVolumes", dailyVolumes);
        model.addAttribute("todayTotalVolume", todayTotalVolume);
        
        return "dashboard";
    }
}