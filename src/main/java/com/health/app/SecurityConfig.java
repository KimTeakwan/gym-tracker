package com.health.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration 
@EnableWebSecurity 
public class SecurityConfig {

    // 🏋️‍♂️ 1. 출입 통제 규칙 설정 (SecurityFilterChain)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // ✨ [핵심 추가] "/verify/**" 경로를 추가했음~ 메일 링크 클릭했을 때 로그인 없이도 들어올 수 있어야 함!
                .requestMatchers("/css/**", "/js/**", "/images/**", "/signup", "/api/members/add", "/verify/**").permitAll() 
                .anyRequest().authenticated() 
            )
            .formLogin(form -> form
                .loginPage("/login") 
                // 💡 [참고] 시큐리티는 enabled 필드가 false인 유저가 로그인하려 하면 알아서 거부함!
                .defaultSuccessUrl("/dashboard", true)
                .permitAll() 
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout") 
                .permitAll()
            )
            // 💡 TiDB 연결 테스트를 위해 CSRF는 잠시 꺼뒀음~ 나중에 바프 끝나고 켜보자고! ㅋㅋㅋ
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // 🏋️‍♂️ 2. 비밀번호 암호화 기계 설치 (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }
}