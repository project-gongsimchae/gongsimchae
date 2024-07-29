package techit.gongsimchae.domain.mail.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import techit.gongsimchae.domain.mail.service.EmailService;

@Component
@Slf4j
@RequiredArgsConstructor
public class MailEventHandler {
    private final EmailService emailService;

    @TransactionalEventListener
    @Async("customTaskExecutor")
    public void joinEventHandler(JoinMailEvent event) throws InterruptedException {
        Thread.sleep(2000);
        emailService.sendJoinEmail(event);
        log.debug("Email send to {}", event);
    }

    @EventListener
    @Async("customTaskExecutor")
    public void authEventHandler(AuthCodeEvent event) {
        emailService.sendCodeMail(event);
        log.debug("Email send to {}", event);
    }
}
