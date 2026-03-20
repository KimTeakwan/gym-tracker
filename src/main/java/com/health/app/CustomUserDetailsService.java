package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // 💡 "스프링아, 이 녀석이 이제부터 DB 조회 담당 비서다!" 선언!
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 유저가 입력한 아이디로 DB(TiDB)를 싹 뒤져봄!
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("없는 아이디입니다! 헬스장 등록부터 하세요! 😡"));

        // 2. DB에서 찾은 진짜 회원 정보를 시큐리티 관장님이 이해할 수 있는 서류(UserDetails)로 바꿔서 제출함!
        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword()) // 💡 암호화된 비번 그대로 넘기면 관장님이 알아서 비교함!
                .roles(member.getRole())
                .build();
    }
}