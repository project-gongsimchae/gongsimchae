package techit.gongsimchae.domain.portion.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.portion.chatroom.entity.ChatRoom;
import techit.gongsimchae.domain.portion.chatroomuser.entity.ChatRoomUser;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRespDto {
    private String roomId;    // 채팅방 ID
    private String roomName;  // 채팅방 이름
    private int userCount;    // 현재 사용자 수
    private int maxUserCnt; // 최대 유저수

    private List<ChatRoomUser> userlist = new ArrayList<>(); // 해당 방에 들어가있는 유저 목록

    public ChatRoomRespDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.roomName = chatRoom.getRoomName();
        this.maxUserCnt = chatRoom.getMaxUserCnt();
        this.userlist = chatRoom.getChatRoomUsers();
        this.userCount = userlist.size();

    }
}
