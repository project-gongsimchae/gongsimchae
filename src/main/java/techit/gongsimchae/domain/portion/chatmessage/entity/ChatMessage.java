package techit.gongsimchae.domain.portion.chatmessage.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import techit.gongsimchae.domain.portion.chatmessage.dto.ChatMessageDto;

import java.time.LocalDateTime;

@Document("messages")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    @Id
    private String id;

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private LocalDateTime createDate;
    private String s3DataUrl;
    private String fileName;
    private Boolean isRead;
    private String loginId;

    public ChatMessage(ChatMessageDto messagedDto) {
        this.type = messagedDto.getType();
        this.roomId = messagedDto.getRoomId();
        this.sender = messagedDto.getSender();
        this.message = messagedDto.getMessage();
        this.createDate = messagedDto.getCreateDate();
        this.s3DataUrl = messagedDto.getS3DataUrl();
        this.fileName = messagedDto.getFileName();
        this.loginId = messagedDto.getLoginId();
    }
}
