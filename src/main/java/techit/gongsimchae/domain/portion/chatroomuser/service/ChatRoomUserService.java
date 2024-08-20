package techit.gongsimchae.domain.portion.chatroomuser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.portion.chatmessage.dto.ChatMessageDto;
import techit.gongsimchae.domain.portion.chatroomuser.entity.ChatRoomUser;
import techit.gongsimchae.domain.portion.chatroomuser.repository.ChatRoomUserRepository;
import techit.gongsimchae.domain.portion.notifications.event.ChatNotiEvent;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ChatRoomUserService {

    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ApplicationEventPublisher publisher;

    /**
     * 비활성화되어있는 유저에게 알림을 보낸다
     */
    @Transactional
    public void getUserInactiveAndNotNotified(ChatMessageDto chatMessageDto) {
        List<ChatRoomUser> chatRoomUsers = chatRoomUserRepository.findAllByInactiveAndNotNotified(chatMessageDto.getRoomId());
        for (ChatRoomUser chatRoomUser : chatRoomUsers) {
            publisher.publishEvent(new ChatNotiEvent(chatRoomUser.getUser(), chatRoomUser.getChatRoom().getRoomName(),chatMessageDto.getRoomId()) );
            chatRoomUser.sendNotification();
        }
    }

    /**
     * 방을 떠났을 때 비활성화
     */
    @Transactional
    public void disableChatRoomOnLeave(String roomId, String nickname) {
        Optional<ChatRoomUser> _chatRoomUser = chatRoomUserRepository.findByChatUser(roomId, nickname);
        if (_chatRoomUser.isPresent()) {
            ChatRoomUser chatRoomUser = _chatRoomUser.get();
            chatRoomUser.deactivateUser();
        }
    }

    /**
     * 채팅방에 해당 유저가 있는지 확인
     */
    public boolean isUserAlreadyInRoom(String roomId, String nickname) {
        boolean present = chatRoomUserRepository.findByChatUser(roomId, nickname).isPresent();
        log.debug("check user present {}", present);
        return present;
    }

    /**
     * 방에 들어오면 다시 활성화
     */
    @Transactional
    public void activateUser(ChatMessageDto message) {
        Optional<ChatRoomUser> _chatRoomUser = chatRoomUserRepository.findByChatUser(message.getRoomId(), message.getSender());
        if(_chatRoomUser.isPresent()) {
            ChatRoomUser chatRoomUser = _chatRoomUser.get();
            chatRoomUser.activateUser();
        }
    }
}
