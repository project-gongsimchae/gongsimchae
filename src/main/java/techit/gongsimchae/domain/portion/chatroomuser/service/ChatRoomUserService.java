package techit.gongsimchae.domain.portion.chatroomuser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.portion.chatmessage.dto.ChatMessageDto;
import techit.gongsimchae.domain.portion.chatroom.entity.ChatRoom;
import techit.gongsimchae.domain.portion.chatroom.repository.ChatRoomRepository;
import techit.gongsimchae.domain.portion.chatroomuser.entity.ChatRoomUser;
import techit.gongsimchae.domain.portion.chatroomuser.event.RoomUserEndEvent;
import techit.gongsimchae.domain.portion.chatroomuser.repository.ChatRoomUserRepository;
import techit.gongsimchae.domain.portion.notifications.event.ChatNotiEvent;
import techit.gongsimchae.domain.portion.notifications.event.FeedbackNotiEvent;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ChatRoomUserService {

    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ApplicationEventPublisher publisher;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 비활성화되어있는 유저에게 알림을 보낸다
     */

    public void getUserInactiveAndNotNotified(ChatMessageDto chatMessageDto) {
        List<String> loginIds = redisTemplate.opsForHash().entries(chatMessageDto.getRoomId())
                .entrySet().stream().filter(u -> u.getValue().equals(0)).map(i -> (String) i.getKey()).toList();

        List<ChatRoomUser> chatRoomUsers = chatRoomUserRepository.findAllByInactiveAndNotNotified(chatMessageDto.getRoomId(),loginIds);
        for (ChatRoomUser chatRoomUser : chatRoomUsers) {
            publisher.publishEvent(new ChatNotiEvent(chatRoomUser.getUser(), chatRoomUser.getChatRoom().getRoomName(),chatMessageDto.getRoomId()) );
            redisTemplate.opsForHash().put(chatMessageDto.getRoomId(), chatRoomUser.getUser().getLoginId(), 1);
        }
    }

    /**
     * 자기자신을 제외한 채팅방에 참여한 유저들에게 피드백 날리기
     */
    public void sendFeedbackToOthers(RoomUserEndEvent event) {
        ChatRoom chatRoom = chatRoomRepository.findBySubdivisionId(event.getSubdivisionId()).orElseThrow(() -> new CustomWebException(ErrorMessage.SUBDIVISION_NOT_FOUND));
        List<ChatRoomUser> chatRoomUsers = chatRoomUserRepository.findAllByChatRoomId(chatRoom.getId());
        List<User> users = new ArrayList<>();
        for (ChatRoomUser chatRoomUser : chatRoomUsers) {
            users.add(chatRoomUser.getUser());
        }
        publisher.publishEvent(new FeedbackNotiEvent(users,event.getTitle()));
    }

    /**
     * 방을 떠났을 때 비활성화
     */
    @Transactional
    public void disableChatRoomOnLeave(String roomId, String loginId) {
        redisTemplate.opsForHash().put(roomId, loginId, 0);
    }

    /**
     * 채팅방에 해당 유저가 있는지 확인
     */
    public boolean isUserAlreadyInRoom(String roomId, String loginId) {
        boolean result = redisTemplate.opsForHash().entries(roomId).entrySet().stream().anyMatch(u -> u.getKey().equals(loginId));
        log.debug("check user present {}", result);
        return result;
    }

    /**
     * 방에 들어오면 다시 활성화
     */
    @Transactional
    public void activateUser(ChatMessageDto message) {
        redisTemplate.opsForHash().put(message.getRoomId(), message.getLoginId(), 1);
    }
}
