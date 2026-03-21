package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value; // 💡 요게 추가됐음!
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    // 💡 application.yaml의 app.url 설정을 자동으로 가져옴!
    @Value("${app.url}")
    private String appUrl;

    public void sendVerificationEmail(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("🔥 짐 트래커 인증 메일임! 했음~");
            
            // 💡 localhost 대신 실제 서버 주소가 들어가도록 수정했음!
            String verificationLink = appUrl + "/verify?code=" + code;
            String content = "<p>관장님!! 아래 링크 누르고 근성장 시작하셈!!</p>" +
                             "<a href='" + verificationLink + "'>인증하기</a>";
            
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }
}