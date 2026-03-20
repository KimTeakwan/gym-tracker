package com.health.app;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 💡 로그인할 때 쓸 아이디 (중복 불가!)
    @Column(unique = true, nullable = false)
    private String username;

    // 💡 시큐리티 관장님이 쓸 암호화된 비밀번호
    @Column(nullable = false)
    private String password;

    // 💡 화면에 보여줄 닉네임 (예: 헬창아저씨)
    @Column(nullable = false)
    private String nickname;
    
    // 💡 권한 설정 (일반 회원은 전부 'USER'로 통일!)
    private String role = "USER"; 
}