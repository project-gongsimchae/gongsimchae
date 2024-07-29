package techit.gongsimchae.global.message.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendSimpleEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("[공심채] 회원가입을 축하합니다.");
        message.setTo(email);
        message.setTo("테스트용");
        javaMailSender.send(message);
    }
}
