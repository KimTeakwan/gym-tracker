package com.health.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 💡 스프링한테 "이건 환경설정 파일이야!" 하고 알려주는 거임~
@EnableWebSecurity // 💡 "지금부터 웹 보안 관장님 출근하십니다!" 선언!
public class SecurityConfig {

    // 🏋️‍♂️ 1. 출입 통제 규칙 설정 (SecurityFilterChain)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**", "/signup", "/api/members/add").permitAll() 
                .anyRequest().authenticated() 
            )
            .formLogin(form -> form
                // 💡 [핵심 추가] "우리가 만든 /login 화면을 로그인 페이지로 쓸 거야!"
                .loginPage("/login") 
                .defaultSuccessUrl("/dashboard", true)
                .permitAll() 
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout") // 로그아웃하면 다시 로그인 화면으로!
                .permitAll()
            )
            // 💡 TiDB 연결 테스트할 때 에러 안 나게 일단 CSRF(위조 방지) 기능은 꺼둠! (나중에 켤 거임)
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    /* 
    // 🏋️‍♂️ 2. 임시 헬스장 회원 (In-Memory User) 발급!
    // DB 연결 전까지 테스트용으로 쓸 "admin / 1234" 계정을 맥북 메모리에 띄워두는 거임!
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("admin")
            // 💡 비밀번호는 무조건 암호화해야 관장님이 화 안 냄! (1234를 암호화한 거임)
            .password(passwordEncoder().encode("1234")) 
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(user);
    }
    */

    // 🏋️‍♂️ 3. 비밀번호 암호화 기계 설치 (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 해커도 못 뚫는 아주 강력한 암호화 방식임! 😎
    }
}