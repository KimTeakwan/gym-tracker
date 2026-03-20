package com.health.app;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    
    // ✨ [신규] 인증 코드로 회원을 찾는 기능 추가 했음~ [cite: 2026-01-11]
    Member findByVerificationCode(String code);
}