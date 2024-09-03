package techit.gongsimchae.domain.portion.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.portion.chatroom.entity.ChatRoom;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRespDto {
    private String roomId;    // 채팅방 ID
    private String roomName;  // 채팅방 이름
    private Long userCount;    // 현재 사용자 수
    private int maxUserCnt; // 최대 유저수
    private String subDivisionUID;

    public ChatRoomRespDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.roomName = chatRoom.getRoomName();
        this.maxUserCnt = chatRoom.getMaxUserCnt();
        this.subDivisionUID = chatRoom.getSubdivision().getUID();

    }

    public ChatRoomRespDto(String roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }
}
