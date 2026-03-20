// MailService.java 파일 내용임! 했음~ [cite: 2026-01-11]
package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage; // 💡 요 임포트가 중요함!

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("🔥 짐 트래커 인증 메일임! 했음~");
            String content = "<a href='http://localhost:8080/verify?code=" + code + "'>인증하기</a>";
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) { e.printStackTrace(); }
    }
}