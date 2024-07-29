package techit.gongsimchae.global.message.mail.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import techit.gongsimchae.global.message.mail.service.EmailService;

@Component
@Slf4j
@RequiredArgsConstructor
public class MailEventHandler {
    private final EmailService emailService;

    @TransactionalEventListener
    @Async("customTaskExecutor")
    public void handlerMailSendEvent(JoinMailEvent event) {
        emailService.sendSimpleEmail(event.getEmail());
        log.info("Email sent to {}", event);
    }
}
