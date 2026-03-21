package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import java.util.UUID;
import com.health.app.MailService;

import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    
    // ✨ [추가] 메일을 실제로 쏴주는 비서를 고용했음!! 했음~ [cite: 2026-01-11]
    private final MailService mailService; 

    // 1. 회원가입 페이지 보여주기 (GET) 했음~
    @GetMapping("/signup")
    public String signupForm(org.springframework.ui.Model model) { // Model 임포트 대신 풀네임 썼음!
        model.addAttribute("member", new Member());
        return "signup";
    }

    // 2. [통합] 회원가입 처리 로직 (유효성 검사 + 이메일 인증) 했음~
    @PostMapping("/signup")
    public String register(@Valid @ModelAttribute("member") Member member, BindingResult result) {
        
        // [검사] 이모티콘 빌런이나 글자 수 미달 컷!! 했음~
        if (result.hasErrors()) {
            return "signup";
        }

        // [보안] 비밀번호 암호화 가즈아!! 했음~
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        
        // [인증] 이메일 인증 코드 생성 및 계정 잠금 했음~
        String code = UUID.randomUUID().toString();
        member.setVerificationCode(code);
        member.setEnabled(false);
        
        // [저장] TiDB에 관원 등록!! 했음~
        memberRepository.save(member);
        
        // [발송] 진짜 메일 쏴주기!! 했음~
        mailService.sendVerificationEmail(member.getEmail(), code);
        
        return "redirect:/login?verifyEmail";
    }

    // 3. 로그인 페이지 보여주기 했음~
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // 🏋️‍♂️ [핵심] 회원가입 처리 로직 (이메일 인증 버전!)
    @PostMapping("/api/members/add")
    public RedirectView addMember(@ModelAttribute Member member) {
        
        // 1. [기존 유지] 비밀번호 암호화 했음~ [cite: 2026-01-11]
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        
        // 2. ✨ [신규] 겹치지 않는 랜덤 인증 코드 생성 했음~ [cite: 2026-01-11]
        String code = UUID.randomUUID().toString();
        member.setVerificationCode(code);
        
        // 3. ✨ [신규] 계정 상태를 '잠금(false)'으로 설정! (인증 전까지 로그인 못 함!) 했음~
        member.setEnabled(false);
        
        // 4. [기존 유지] DB에 저장 했음~ [cite: 2026-01-11]
        memberRepository.save(member);
        
        // 5. ✨ [신규] 입력한 이메일로 인증 메일 발송!! 했음~ [cite: 2026-01-11]
        mailService.sendVerificationEmail(member.getEmail(), code);
        
        // 6. 🚀 로그인 화면으로 보내면서 "메일 확인해봐!"라고 파라미터 던져줌! 했음~
        return new RedirectView("/login?verifyEmail");
    }

    // ✨ [신규] 메일 링크 클릭 시 "계정 활성화" 시켜주는 주소!! 했음~ [cite: 2026-01-11]
    @GetMapping("/verify")
    public String verifyMember(@RequestParam("code") String code) {
        // 코드로 유저를 찾아서 있으면 enabled를 true로 바꾸고 살려줌! 했음~
        Member member = memberRepository.findByVerificationCode(code);
        if (member != null) {
            member.setEnabled(true);
            member.setVerificationCode(null); // 코드는 한 번 쓰면 버려야지!
            memberRepository.save(member);
            return "redirect:/login?verified";
        }
        return "redirect:/login?error";
    }
}