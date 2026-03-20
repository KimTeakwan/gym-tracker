package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView; // 💡 추가! 했음~

@RestController
@RequestMapping("/api/cardio")
@RequiredArgsConstructor
public class CardioWorkoutController {

    private final CardioWorkoutRepository repository;
    private final MemberRepository memberRepository;

    @PostMapping
    public ResponseEntity<String> save(@RequestBody CardioWorkout workout, 
                                     @AuthenticationPrincipal UserDetails userDetails) {
        Member currentMember = memberRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없음!"));
        workout.setMember(currentMember);
        repository.save(workout);
        return ResponseEntity.ok("저장 성공!");
    }

    // ✨ [수정] 삭제 후 유산소 탭(?tab=cardio)으로 리다이렉트!! 했음
    @GetMapping("/delete/{id}")
    public RedirectView deleteCardio(@PathVariable("id") Long id) {
        repository.deleteById(id);
        return new RedirectView("/dashboard?tab=cardio");
    }
}