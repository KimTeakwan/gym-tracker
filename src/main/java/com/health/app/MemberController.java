package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model; // 💡 명시적으로 임포트 추가했음!
import java.util.UUID;
import jakarta.validation.Valid; // 💡 심판(유효성 검사) 도구 했음~

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    
    // ✨ [추가] 메일을 실제로 쏴주는 비서를 고용했음!! 했음~ [cite: 2026-01-11]
    private final MailService mailService; 

    // 1. 회원가입 페이지 보여주기 (GET) 했음~
    @GetMapping("/signup")
    public String signupForm(Model model) {
        // 💡 빈 회원 바구니를 만들어 보내야 에러 전광판이 작동함!!
        model.addAttribute("member", new Member());
        return "signup";
    }

    // 2. [통합 완성본] 회원가입 처리 로직 (유효성 검사 + 보안 + 이메일 인증) 했음~ [cite: 2026-03-21]
    // 💡 기존의 /api/members/add는 보안상 위험해서 삭제하고 이쪽으로 통합했음!! 했음~
    @PostMapping("/signup")
    public String register(@Valid @ModelAttribute("member") Member member, BindingResult result) {
        
        // [1단계: 심판 판정] 아이디/비번 규칙 어기면 바로 컷트!! 했음~ [cite: 2026-03-21]
        if (result.hasErrors()) {
            return "signup"; // 에러 메시지 들고 다시 가입창으로 돌아감!! 했음~
        }

        // [2단계: 보안 강화] 비밀번호 암호화 가즈아!! 했음~
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        
        // [3단계: 인증 준비] 겹치지 않는 랜덤 코드 생성 및 계정 잠금 했음~ [cite: 2026-03-21]
        String code = UUID.randomUUID().toString();
        member.setVerificationCode(code);
        member.setEnabled(false); // 💡 인증 전까지는 로그인 못 함!!
        
        // [4단계: 저장] TiDB에 우리 관원 등록!! 했음~
        memberRepository.save(member);
        
        // [5단계: 발송] 입력한 메일로 진짜 인증 메일 쏴주기!! 했음~ [cite: 2026-01-11]
        mailService.sendVerificationEmail(member.getEmail(), code);
        
        // 🚀 성공 시 로그인 화면으로 보내면서 안내 파라미터 던져줌!! 했음~
        return "redirect:/login?verifyEmail";
    }

    // 3. 로그인 페이지 보여주기 했음~
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // 4. ✨ [신규] 메일 링크 클릭 시 "계정 활성화" 시켜주는 주소!! 했음~ [cite: 2026-01-11]
    @GetMapping("/verify")
    public String verifyMember(@RequestParam("code") String code) {
        // 코드로 유저를 찾아서 있으면 enabled를 true로 바꾸고 살려줌! 했음~
        Member member = memberRepository.findByVerificationCode(code);
        if (member != null) {
            member.setEnabled(true);
            member.setVerificationCode(null); // 코드는 일회용이니 삭제!! 했음~
            memberRepository.save(member);
            return "redirect:/login?verified";
        }
        return "redirect:/login?error";
    }
}