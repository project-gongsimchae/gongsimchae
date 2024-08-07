package techit.gongsimchae.domain.portion.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.portion.chatroom.entity.ChatRoom;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionRespDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRespDto {

    private Long id;
    private String message;
    private LocalDateTime createDate;
    private UserRespDtoWeb user;
    private SubdivisionRespDto subdivision;

    public ChatRoomRespDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.message = chatRoom.getMessage();
        this.createDate = chatRoom.getCreateDate();
        this.user = new UserRespDtoWeb(chatRoom.getUser());
        this.subdivision = new SubdivisionRespDto(chatRoom.getSubdivision());
    }
}
