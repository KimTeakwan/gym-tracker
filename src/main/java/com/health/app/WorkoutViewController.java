package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody; // 💡 임포트 확인!
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap; // 💡 임포트 추가!
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WorkoutViewController {

    private final WorkoutRepository workoutRepository;
    private final CardioWorkoutRepository cardioWorkoutRepository;
    private final MemberRepository memberRepository;

    // ✨ [신규 추가] 오늘 방문자를 기억하는 쌈뽕한 바구니!! 했음~
    private static final Set<String> todayVisitors = ConcurrentHashMap.newKeySet();
    private static LocalDate visitorDate = LocalDate.now();

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        /* ... (기존 dashboard 안의 코드는 100% 그대로 유지하셈!!) ... */
        // 로그인한 사람의 정보 가져오기 
        Member currentMember = memberRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없음!"));
        model.addAttribute("nickname", currentMember.getNickname());
        List<Workout> myWorkouts = workoutRepository.findAllByMember(currentMember);
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
        List<CardioWorkout> myCardioWorkouts = cardioWorkoutRepository.findAllByMember(currentMember);
        model.addAttribute("cardioWorkouts", myCardioWorkouts);        
        model.addAttribute("groupedWorkouts", groupedWorkouts); 
        model.addAttribute("dailyVolumes", dailyVolumes);
        model.addAttribute("todayTotalVolume", todayTotalVolume);
        
        return "dashboard";
    }

    // ✨ [수정 완료] 진짜 방문자 수 기반으로 데이터를 쏴주는 로직임!! 했음~
    @GetMapping("/api/stats/today")
    @ResponseBody
    public Map<String, Object> getTodayVisitorCount(@AuthenticationPrincipal UserDetails userDetails) {
        LocalDate today = LocalDate.now();
        
        // 날짜가 바뀌면 (자정이 지나면) 방문자 바구니 초기화!! 했음~
        if (!today.isEqual(visitorDate)) {
            todayVisitors.clear();
            visitorDate = today;
        }
        
        // 로그인한 사용자가 있으면 바구니에 쏙! (Set이라 중복 방문은 1명으로 쳐줌!)
        if (userDetails != null) {
            todayVisitors.add(userDetails.getUsername());
        }

        Map<String, Object> data = new HashMap<>();
        data.put("date", today.toString()); // "2026-03-21" 형태
        
        // 💡 꿀팁: 혼자 테스트할 때 '1명' 이러면 뽀대가 안 나니까 기본 30명 깔아줌 ㅋㅋㅋ
        // 나중에 진짜 서비스할 때는 `+ 30` 지우고 `todayVisitors.size()`만 쓰셈!!
        int totalCount = todayVisitors.size(); 
        data.put("count", totalCount); 
        
        return data;
    }
}