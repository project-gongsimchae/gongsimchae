package techit.gongsimchae.domain.common.user.mail.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import techit.gongsimchae.domain.common.user.mail.service.EmailService;

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
        log.debug("Join Email send to {}", event);
    }

    @EventListener
    @Async("customTaskExecutor")
    public void authEventHandler(AuthCodeEvent event) {
        emailService.sendCodeMail(event);
        log.debug("authCode Email send to {}", event);
    }

    @Async("customTaskExecutor")
    @EventListener
    public void FindPwEventHandler(PasswordEvent event) {
        emailService.sendPasswordEmail(event);
        log.debug("find Pw Email send to {}", event);
    }

    @Async("customTaskExecutor")
    @EventListener
    public void findIdEventHandler(LoginIdEvent event) {
        emailService.sendLoginId(event);
        log.debug("find Id Email send to {}", event);
    }
}
