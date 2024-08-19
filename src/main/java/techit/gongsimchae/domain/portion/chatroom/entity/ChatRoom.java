package techit.gongsimchae.domain.portion.chatroom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.portion.chatroomuser.entity.ChatRoomUser;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "chat_room")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String roomId;
    private String roomName;
    private int maxUserCnt;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomUser> chatRoomUsers = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Subdivision subdivision;

    public ChatRoom(Subdivision subdivision) {
        this.roomId = UUID.randomUUID().toString();
        this.roomName = subdivision.getTitle();
        this.maxUserCnt = subdivision.getNumOfParticipants();
        this.subdivision = subdivision;
    }
}
