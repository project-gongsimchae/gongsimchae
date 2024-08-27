package techit.gongsimchae.domain.portion.chatroomuser.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import techit.gongsimchae.domain.portion.chatroomuser.service.ChatRoomUserService;
import techit.gongsimchae.domain.portion.notifications.event.InquiryNotiEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class RoomUserEventHandler {

    private final ChatRoomUserService chatRoomUserService;

    @TransactionalEventListener
    @Async("customTaskExecutor")
    public void getChatRoomUsers(RoomUserEndEvent event) throws InterruptedException {
        Thread.sleep(1000);
        chatRoomUserService.sendFeedbackToOthers(event);
        log.debug("noti to inquiry {}", event);
    }
}
