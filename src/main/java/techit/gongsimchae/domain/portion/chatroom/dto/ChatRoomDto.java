package techit.gongsimchae.domain.portion.chatroom.dto;

import lombok.Getter;
import lombok.Setter;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.portion.chatroom.entity.ChatRoom;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatRoomDto {

    private Long id;
    private String message;
    private LocalDateTime createDate;
    private UserRespDtoWeb user;
    private SubdivisionDto subdivision;

    public ChatRoomDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.message = chatRoom.getMessage();
        this.createDate = chatRoom.getCreateDate();
        this.user = new UserRespDtoWeb(chatRoom.getUser());
        this.subdivision = new SubdivisionDto(chatRoom.getSubdivision());
    }
}
