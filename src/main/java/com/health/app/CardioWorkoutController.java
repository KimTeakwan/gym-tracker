package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/api/cardio")
@RequiredArgsConstructor
public class CardioWorkoutController {

    private final CardioWorkoutRepository repository;
    private final MemberRepository memberRepository; // 💡 회원 식별을 위해 추가! [cite: 2026-03-15]

    // 💡 [핵심 수정] 유산소 저장 시 '내 이름표' 달아주기! [cite: 2026-03-15, 2026-03-20]
    @PostMapping("/add")
    public RedirectView addCardio(@ModelAttribute CardioWorkout workout, 
                                 @AuthenticationPrincipal UserDetails userDetails) {
        // 1. 현재 로그인한 사람 찾기 [cite: 2026-03-15]
        Member currentMember = memberRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없음!"));
        
        // 2. 기록에 주인 설정 [cite: 2026-03-15]
        workout.setMember(currentMember);
        repository.save(workout);
        
        // 3. 대시보드로 다시 튕겨줌! [cite: 2026-03-20]
        return new RedirectView("/dashboard");
    }

    // ✅ 기존 기능 유지: 삭제 로직 [cite: 2026-03-20]
    @GetMapping("/delete/{id}")
    public RedirectView deleteCardio(@PathVariable("id") Long id, 
                                    @AuthenticationPrincipal UserDetails userDetails) {
        CardioWorkout workout = repository.findById(id).orElseThrow();
        
        // 💡 보안 체크: 내 것만 지울 수 있게! [cite: 2026-03-15]
        if (workout.getMember().getUsername().equals(userDetails.getUsername())) {
            repository.delete(workout);
        }
        return new RedirectView("/dashboard"); 
    }
}