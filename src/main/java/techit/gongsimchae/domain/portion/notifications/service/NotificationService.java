package techit.gongsimchae.domain.portion.notifications.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.portion.feedback.dto.FeedbackUserRespDtoWeb;
import techit.gongsimchae.domain.portion.notifications.dto.NotificationRespDtoWeb;
import techit.gongsimchae.domain.portion.notifications.dto.NotificationResponse;
import techit.gongsimchae.domain.portion.notifications.entity.NotificationType;
import techit.gongsimchae.domain.portion.notifications.entity.Notifications;
import techit.gongsimchae.domain.portion.notifications.event.ChatNotiEvent;
import techit.gongsimchae.domain.portion.notifications.event.FeedbackNotiEvent;
import techit.gongsimchae.domain.portion.notifications.event.GroupBuyingNotiEvent;
import techit.gongsimchae.domain.portion.notifications.event.KeywordNotiEvent;
import techit.gongsimchae.domain.portion.notifications.repository.EmitterRepository;
import techit.gongsimchae.domain.portion.notifications.repository.NotificationRepository;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 1000 * 60 * 5L;
    private static final String INQUIRY_URL = "/mypage/inquiry/list";
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    public SseEmitter subscribe(PrincipalDetails principalDetails, String lastEventId) {
        Long userId = getCurrentUser(principalDetails).getId();
        String id = userId + "_" + System.currentTimeMillis();

        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));



        // 오류방지를 위한 더미 이벤트 전송
        sendToClient(emitter, id, new NotificationResponse("EventStream Created, [userId=" + id+"]"));

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 방지
        if (!lastEventId.isBlank()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(userId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }
        return emitter;

    }

    /**
     * 채팅방 알림을 알려주는 메서드
     */
    @Transactional
    public void alertAboutChat(ChatNotiEvent chatNotiEvent) {
        Notifications notifications = Notifications.builder()
                .user(chatNotiEvent.getUser()).isRead(0).url("/chat/room?roomId=" + chatNotiEvent.getUrl())
                .notificationType(NotificationType.CHAT).content("["+chatNotiEvent.getContent()+"방]" + " 새로운 메시지가 왔습니다.").build();

        notificationRepository.save(notifications);
    }

    /**
     * 1:1 문의 답변이 왔을 때 알려주는 메서드
     */
    @Transactional
    public void alertAboutInquiry(User receiver, String content) {
        Notifications notifications = Notifications.builder()
                .user(receiver).isRead(0).url(INQUIRY_URL).content(content).notificationType(NotificationType.INQUIRY).build();
        notificationRepository.save(notifications);
        NotificationResponse notificationResponse = new NotificationResponse(notifications);

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartById(String.valueOf(receiver.getId()));
        log.debug("sseEmitters: {}", sseEmitters);
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notifications);
                    sendToClient(emitter, key, notificationResponse);
                }
        );
    }

    /**
     * 키워드 알림 메서드
     */
    @Transactional
    public void alertAboutKeyword(KeywordNotiEvent event) {
        Notifications notifications = Notifications.builder()
                .user(event.getUser()).isRead(0).url("/portioning/" + event.getUrl())
                .notificationType(NotificationType.KEYWORD).content("["+event.getKeyword()+"] "+event.getTitle() +" ("+event.getAddress()+")").build();

        notificationRepository.save(notifications);
    }

    @Transactional
    public void alertAboutFeedback(FeedbackNotiEvent event) {
        try {
            String url = UUID.randomUUID().toString();
            String dtos = objectMapper.writeValueAsString(FeedbackUserRespDtoWeb.toDto(event.getUsers()));
            redisTemplate.opsForValue().set(url, dtos);
            for (User user : event.getUsers()) {
                Notifications notifications = Notifications.builder()
                        .user(user).isRead(0).notificationType(NotificationType.FEEDBACK)
                        .content("[" + event.getTitle() + "] 리뷰를 남길 수 있습니다.").url("/portioning/feedback/write?url="+url).build();

                notificationRepository.save(notifications);

            }

        } catch (Exception e) {
            throw new CustomWebException(e);
        }

    }

    public  NotificationResponse getAllNotifications(PrincipalDetails principalDetails) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException("not found user"));
        List<NotificationRespDtoWeb> result = notificationRepository.findAllUserId(user.getId()).stream()
                .map(NotificationRespDtoWeb::new).collect(Collectors.toList());

        long unreadCount = result.stream().filter(n -> !n.getIsRead().equals(0)).count();
        return new NotificationResponse(result, unreadCount);
    }

    /**
     * 모든 알림창을 읽음표시로 해주는 메서드
     */
    @Transactional
    public void readNotification(PrincipalDetails principalDetails) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException("not found user"));
        notificationRepository.findAllUnreadNotificationsByUser(user.getId()).forEach(Notifications::read);
    }

    private void sendToClient(SseEmitter emitter, String id, Object data) {

        try{
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(objectMapper.writeValueAsString(data)));
            log.debug("emitter {}, id = {}, data = {}",emitter,id,data);
        } catch (IOException e) {
            emitterRepository.deleteById(id);
            log.error("SSE 연결 오류! ", e);
        }
    }

    /**
     * SecurityContext에서 유저를 찾는 메서드
     */
    private User getCurrentUser(PrincipalDetails principalDetails) {
        return userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * 공동구매 성공/실패 여부 알림 메서드
     * @param event
     */
    @Transactional
    public void alertAboutGroupBuying(GroupBuyingNotiEvent event) {
        Notifications notifications = Notifications.builder()
                .user(event.getUser())
                .isRead(0)
                .url("/mypage/orders")
                .content(event.getContent())
                .notificationType(NotificationType.GROUP_BUYING)
                .build();

        notificationRepository.save(notifications);
    }
}