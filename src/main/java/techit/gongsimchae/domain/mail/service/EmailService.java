package techit.gongsimchae.domain.mail.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.mail.event.AuthCodeEvent;
import techit.gongsimchae.domain.mail.event.JoinMailEvent;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendJoinEmail(JoinMailEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("[공심채] 회원가입을 축하합니다.");
        message.setTo(event.getEmail());
        message.setText("테스트용");
        javaMailSender.send(message);
    }

    public void sendCodeMail(AuthCodeEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("[공심채] 인증번호입니다.");
        message.setTo(event.getEmail());
        message.setText("인증 코드는 "+event.getCode()+ " 입니다.");
        javaMailSender.send(message);
    }
}
