package techit.gongsimchae.domain.portion.notifications.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.portion.notifications.dto.NotificationRespDtoWeb;
import techit.gongsimchae.domain.portion.notifications.dto.NotificationResponse;
import techit.gongsimchae.domain.portion.notifications.entity.NotificationType;
import techit.gongsimchae.domain.portion.notifications.entity.Notifications;
import techit.gongsimchae.domain.portion.notifications.repository.EmitterRepository;
import techit.gongsimchae.domain.portion.notifications.repository.NotificationRepository;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 1000 * 60 * 5L;
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public SseEmitter subscribe(PrincipalDetails principalDetails, String lastEventId) {
        Long userId = getCurrentUser(principalDetails).getId();
        String id = userId + "_" + System.currentTimeMillis();

        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        // 오류방지를 위한 더미 이벤트 전송
        sendToClient(emitter, id, "EventStream Created, [userId=" + id+"]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 방지
        if (!lastEventId.isBlank()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(userId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }
        return emitter;

    }
    
    @Transactional
    public void alertAboutInquiry(User receiver, String content) {
        Notifications notifications = new Notifications(receiver, content, NotificationType.INQUIRY);
        notificationRepository.save(notifications);

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartById(String.valueOf(receiver.getId()));
        log.debug("sseEmitters: {}", sseEmitters);
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notifications);
                    sendToClient(emitter, key, content);
                }
        );
    }

    public  NotificationResponse findUnreadNotifications(User user) {
        List<NotificationRespDtoWeb> result = notificationRepository.findAllUserId(user.getId()).stream()
                .map(NotificationRespDtoWeb::new).collect(Collectors.toList());

        long unreadCount = result.stream().filter(n -> !n.getIsRead().equals(0)).count();
        return new NotificationResponse(result, unreadCount);
    }

    public void readNotification(Long id) {
        Notifications notifications = notificationRepository.findById(id).orElseThrow(() -> new CustomWebException("not found notifications"));
        notifications.read();
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
}
