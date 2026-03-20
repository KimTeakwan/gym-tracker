package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service 
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 유저 아이디로 DB를 뒤져봄! 했음~ [cite: 2026-01-11]
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("없는 아이디입니다! 헬스장 등록부터 하세요! 😡"));

        // 2. ✨ [핵심 수정] 시큐리티 관장님한테 이 사람이 인증을 했는지 안 했는지 알려줌!! 했음~
        // member.isEnabled()가 false면 시큐리티가 "이 계정은 비활성화 상태임!" 하고 로그인을 막아버림!! [cite: 2026-03-15]
        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .roles(member.getRole())
                .disabled(!member.isEnabled()) // 💡 enabled가 false면 계정을 잠가버림!! 했음~ [cite: 2026-01-11]
                .build();
    }
}