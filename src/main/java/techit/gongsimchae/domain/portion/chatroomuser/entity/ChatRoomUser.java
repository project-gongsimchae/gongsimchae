package techit.gongsimchae.domain.portion.chatroomuser.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.portion.chatroom.entity.ChatRoom;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatRoomUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    private Integer isActivate = 0; // 0 비활성 1 활성
    private Integer isNotifications = 0; // 0 보내지 않음 1 보냈음

    public ChatRoomUser(User user, ChatRoom chatRoom) {
        this.user = user;
        this.isActivate = 1;
        addChatRoom(chatRoom);
    }

    public void addChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        chatRoom.getChatRoomUsers().add(this);
    }

    public void deactivateUser() {
        this.isActivate = 0;
    }

    public void activateUser() {
        this.isActivate = 1;
        this.isNotifications = 0;
    }

    public void sendNotification() {
        this.isNotifications = 1;
    }
}
