package com.health.app;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

@Entity
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$", message = "아이디는 영문, 숫자 조합 4~12자여야 함!!")
    private String username;

    @Column(nullable = false)
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 함!!")
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