package techit.gongsimchae.domain.portion.notifications.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import techit.gongsimchae.domain.portion.notifications.service.NotificationService;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotiEventHandler {
    private final NotificationService notificationService;

    @TransactionalEventListener
    @Async("customTaskExecutor")
    public void SendInquiryAnswer(InquiryNotiEvent event) throws InterruptedException {
        Thread.sleep(2000);
        notificationService.alertAboutInquiry(event.getUser(), event.getContent());
        log.debug("noti to inquiry {}", event);
    }
}
