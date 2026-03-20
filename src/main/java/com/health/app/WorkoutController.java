package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutRepository workoutRepository;
    private final MemberRepository memberRepository; // 💡 회원 식별을 위해 추가!

    // ✅ 기존 기능 유지: 운동 기록 저장 (이제 주인 정보까지 같이 저장함!) [cite: 2026-03-15]
    @PostMapping("/add")
    public RedirectView addWorkout(@ModelAttribute Workout workout, 
                                 @AuthenticationPrincipal UserDetails userDetails) {
        // 현재 로그인한 사람 찾기
        Member currentMember = memberRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없음!"));
        
        workout.setMember(currentMember); // 기록에 주인표 달기!
        workoutRepository.save(workout);
        return new RedirectView("/dashboard");
    }

    // ✅ 기존 기능 업데이트: 전체 조회가 아닌 '내 기록'만 조회로 변경! [cite: 2026-03-15]
    @GetMapping
    public List<Workout> getMyWorkouts(@AuthenticationPrincipal UserDetails userDetails) {
        Member currentMember = memberRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없음!"));
                
        return workoutRepository.findAllByMember(currentMember);
    }

    // ✅ 기존 기능 유지: 기록 삭제 (내 것인지 확인하는 보안 로직 추가!) [cite: 2026-03-15]
    @GetMapping("/delete/{id}")
    public RedirectView deleteWorkout(@PathVariable("id") Long id,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("기록을 찾을 수 없음!"));
        
        // 💡 보안 체크: 삭제하려는 기록의 주인이 로그인한 사람과 일치하는가?
        if (workout.getMember().getUsername().equals(userDetails.getUsername())) {
            workoutRepository.delete(workout);
        }
        
        return new RedirectView("/dashboard");
    }
}