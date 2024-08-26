package techit.gongsimchae.domain.portion.chatroomuser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ChatRoomUserService {

    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ApplicationEventPublisher publisher;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * 비활성화되어있는 유저에게 알림을 보낸다
     */

    public void getUserInactiveAndNotNotified(ChatMessageDto chatMessageDto) {
        List<ChatRoomUser> chatRoomUsers = chatRoomUserRepository.findAllByInactiveAndNotNotified(chatMessageDto.getRoomId());
        for (ChatRoomUser chatRoomUser : chatRoomUsers) {
            publisher.publishEvent(new ChatNotiEvent(chatRoomUser.getUser(), chatRoomUser.getChatRoom().getRoomName(),chatMessageDto.getRoomId()) );
            chatRoomUser.sendNotification();
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
