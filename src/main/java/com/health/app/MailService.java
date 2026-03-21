package com.health.app;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    @Value("${app.url}")
    private String appUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendVerificationEmail(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            // 💡 보낸 사람 주소에 "Gym Tracker 🔥"라는 별명을 달아줬음!! 했음~
            helper.setFrom(fromEmail, "Gym Tracker 🔥");
            
            helper.setTo(to);
            helper.setSubject("🔥 짐 트래커 인증 메일임! 했음~");
            
            String verificationLink = appUrl + "/verify?code=" + code;
            String content = "<p>관장님!! 아래 링크 누르고 근성장 시작하셈!!</p>" +
                             "<a href='" + verificationLink + "'>인증하기</a>";
            
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) { 
            e.printStackTrace(); 
        }
    }
}