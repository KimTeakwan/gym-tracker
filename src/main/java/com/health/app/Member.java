package com.health.app;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // 로그인용 아이디

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;
    
    // ✨ [신규] 진짜 메일을 받을 주소! (중복 불가)
    @Column(unique = true, nullable = false)
    private String email; 

    private String role = "USER"; 

    // ✨ [신규] 이메일 인증을 완료했는지 체크 (기본값 false) 했음~ [cite: 2026-01-11]
    private boolean enabled = false;

    // ✨ [신규] 인증용 랜덤 코드 했음~ [cite: 2026-01-11]
    private String verificationCode;
}