package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor // 💡 이걸 꼭 달아줘야 스프링이 알아서 아래 친구들을 데려옴!
public class MemberController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // 💡 우리가 SecurityConfig에 만들어둔 암호화 기계!

    @GetMapping("/signup")
    public String signupForm() {
        return "signup"; 
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // 🏋️‍♂️ [핵심] 회원가입 처리 로직!
    @PostMapping("/api/members/add")
    public RedirectView addMember(@ModelAttribute Member member) {
        
        // 1. 유저가 입력한 비밀번호를 가져와서 빡세게 암호화함!
        String rawPassword = member.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        // 2. 암호화된 비밀번호로 싹 바꿔치기함!
        member.setPassword(encodedPassword);
        
        // 3. DB에 냅다 저장!
        memberRepository.save(member);
        
        // 4. 회원가입 끝났으면 로그인 화면으로 시원하게 튕겨줌! 🚀
        return new RedirectView("/login");
    }
}