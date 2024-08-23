package techit.gongsimchae.domain.common.user.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.common.user.mail.event.AuthCodeEvent;
import techit.gongsimchae.domain.common.user.mail.event.JoinMailEvent;
import techit.gongsimchae.domain.common.user.mail.event.LoginIdEvent;
import techit.gongsimchae.domain.common.user.mail.event.PasswordEvent;

@Service
@RequiredArgsConstructor
@Slf4j
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

    public void sendPasswordEmail(PasswordEvent event) {
        log.debug("send passwordEvent {} ", event);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("[공심채] 임시 비밀번호입니다.");
        message.setTo(event.getEmail());
        message.setText("비밀번호는 "+event.getPassword()+ " 입니다.");
        javaMailSender.send(message);
    }

    public void sendLoginId(LoginIdEvent event) {
        log.debug("send LoginIdEvent {} ", event);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("[공심채] 아이디를 안내드립니다.");
        message.setTo(event.getEmail());
        message.setText("아이디는 "+event.getLoginId()+ " 입니다.");
        javaMailSender.send(message);
    }
}
