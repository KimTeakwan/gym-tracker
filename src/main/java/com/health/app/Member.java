package com.health.app;

import jakarta.persistence.*;
import jakarta.validation.constraints.*; // 💡 모든 유효성 검사 도구를 한 번에 가져옴! [cite: 2026-03-21]
import lombok.Data;

@Entity
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1. 아이디: 영소문자+숫자 조합, 4~12자 했음~ [cite: 2026-03-21]
    @Column(nullable = false, unique = true)
    @NotBlank(message = "아이디는 필수 입력 사항임!!")
    @Pattern(regexp = "^[a-z0-9]{4,12}$", message = "아이디는 영문 소문자와 숫자 조합으로 4~12자여야 함!!")
    private String username;

    // 2. 비밀번호: 최소 8자 이상 했음~ [cite: 2026-03-21]
    @Column(nullable = false)
    @NotBlank(message = "비밀번호는 필수 입력 사항임!!")
    @Size(min = 8, message = "비밀번호는 영소문자, 숫자, 알파벳을 섞어 8자리 이상 입력해주세요.")
    private String password;

    // 3. 닉네임: 2~10자로 쌈뽕하게 제한했음~ [cite: 2026-03-21]
    @Column(nullable = false)
    @NotBlank(message = "닉네임은 필수 입력 사항임!!")
    @Size(min = 2, max = 10, message = "닉네임은 2~10자 사이여야 함!!")
    private String nickname;
    
    // 4. 이메일: 진짜 이메일 주소인지 심판이 확인함!! 했음~ [cite: 2026-03-21]
    @Column(unique = true, nullable = false)
    @NotBlank(message = "이메일은 필수 입력 사항임!!")
    @Email(message = "올바른 이메일 형식이 아님!! (예: gym@google.com)")
    private String email; 

    private String role = "USER"; 

    // 이메일 인증 여부 (기본값 false) 했음~ [cite: 2026-03-21]
    private boolean enabled = false;

    // 인증용 랜덤 코드 했음~ [cite: 2026-03-21]
    private String verificationCode;
}