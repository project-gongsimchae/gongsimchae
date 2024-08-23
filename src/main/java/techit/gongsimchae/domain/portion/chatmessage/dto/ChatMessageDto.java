package techit.gongsimchae.domain.portion.chatmessage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.portion.chatmessage.entity.ChatMessage;
import techit.gongsimchae.domain.portion.chatmessage.entity.MessageType;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private LocalDateTime createDate;
    private String s3DataUrl;
    private String fileName;
    private Boolean isRead;

    public ChatMessageDto(ChatMessage chatMessage) {
        this.type = chatMessage.getType();
        this.roomId = chatMessage.getRoomId();
        this.sender = chatMessage.getSender();
        this.message = chatMessage.getMessage();
        this.createDate = chatMessage.getCreateDate();
        this.s3DataUrl = chatMessage.getS3DataUrl();
        this.fileName = chatMessage.getFileName();
        this.isRead = chatMessage.getIsRead();
    }
}
