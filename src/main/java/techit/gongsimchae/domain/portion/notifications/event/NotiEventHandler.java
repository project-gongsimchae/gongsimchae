package techit.gongsimchae.domain.portion.notifications.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
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

    @TransactionalEventListener
    @Async("customTaskExecutor")
    public void SendChatNotifications(ChatNotiEvent event) throws InterruptedException {
        Thread.sleep(2000);
        notificationService.alertAboutChat(event);
        log.debug("noti to inquiry {}", event);
    }


    @TransactionalEventListener
    @Async("customTaskExecutor")
    public void SendKeywordNotifications(KeywordNotiEvent event) throws InterruptedException {
        Thread.sleep(2000);
        notificationService.alertAboutKeyword(event);
        log.debug("noti to inquiry {}", event);
    }

    @EventListener
    @Async("customTaskExecutor")
    public void SendFeedbackNotifications(FeedbackNotiEvent event) throws InterruptedException {
        Thread.sleep(2000);
        notificationService.alertAboutFeedback(event);
        log.debug("noti to inquiry {}", event);
    }

    @TransactionalEventListener
    @Async("customTaskExecutor")
    public void SendGroupBuyingNotifications(GroupBuyingNotiEvent event) throws InterruptedException {
        Thread.sleep(2000);
        notificationService.alertAboutGroupBuying(event);
        log.debug("noti to GroupBuying {}", event);
    }
}
