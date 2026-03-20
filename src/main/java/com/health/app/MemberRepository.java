package com.health.app;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 💡 나중에 로그인할 때 "이 아이디 가진 사람 있어?" 하고 물어볼 때 쓸 메서드임!
    Optional<Member> findByUsername(String username);
}