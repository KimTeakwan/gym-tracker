package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cardio")
@RequiredArgsConstructor
public class CardioWorkoutController {

    private final CardioWorkoutRepository repository;
    private final MemberRepository memberRepository;

    @PostMapping
    public ResponseEntity<String> save(@RequestBody CardioWorkout workout, 
                                     @AuthenticationPrincipal UserDetails userDetails) {
        // 1. 현재 로그인한 사용자를 DB에서 확실히 찾아옴!! 했음~ [cite: 2026-03-20]
        Member currentMember = memberRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없음!"));

        // 2. 유산소 기록과 회원을 '강력하게' 결합함!! (이게 자동 연동의 핵심임!) [cite: 2026-03-15]
        workout.setMember(currentMember);
        
        // 3. 저장 실행!! 했음~
        repository.save(workout);
        
        // 4. ✨ 객체 통째로 보내지 말고, 성공 문자열만 보내서 500 에러를 원천 차단함!! [cite: 2026-03-15]
        return ResponseEntity.ok("저장 성공!");
    }

    @GetMapping("/delete/{id}")
    public String deleteCardio(@PathVariable("id") Long id) {
        repository.deleteById(id);
        return "삭제 완료!";
    }
}