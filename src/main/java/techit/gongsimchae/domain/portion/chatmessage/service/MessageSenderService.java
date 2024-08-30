package techit.gongsimchae.domain.portion.chatmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.portion.chatmessage.dto.ChatMessageDto;
import techit.gongsimchae.domain.portion.chatmessage.entity.MessageType;
import techit.gongsimchae.domain.portion.chatroom.service.ChatRoomService;
import techit.gongsimchae.domain.portion.chatroomuser.service.ChatRoomUserService;
import techit.gongsimchae.global.config.kafka.KafkaConstants;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageSenderService {

    private final KafkaTemplate<String, ChatMessageDto> kafkaTemplate;
    private final ChatMessageService chatService;
    private final ChatRoomService chatRoomService;
    private final ChatRoomUserService chatRoomUserService;
    private final ChatClient chatClient;


    public void send(ChatMessageDto message) {

        message.setCreateDate(LocalDateTime.now());

        // Kafka Template 을 사용하여 메세지를 지정된 토픽으로 전송
        try {
            if (message.getType().equals(MessageType.ENTER) && chatRoomUserService.isUserAlreadyInRoom(message.getRoomId(), message.getLoginId())) {
                chatRoomUserService.activateUser(message);
            }
            if (message.getType().equals(MessageType.TALK)) {
                chatService.save(message);
                kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, message);
                chatRoomUserService.getUserInactiveAndNotNotified(message);
            }
            else if (message.getType().equals(MessageType.ENTER) && !chatRoomUserService.isUserAlreadyInRoom(message.getRoomId(), message.getLoginId())) {
                message.setMessage(message.getSender() +"님 입장!!");
                chatRoomService.addUserInChat(message.getRoomId(), message.getLoginId());
                chatService.save(message);
                kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, message);
            } else if (message.getType().equals(MessageType.LEAVE)) {
                message.setMessage(message.getSender() + "님 퇴장!!");
                chatRoomService.deleteUserInChat(message.getRoomId(), message.getLoginId());
                chatService.save(message);
                kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, message);
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void sendToAI(ChatMessageDto message) {

        message.setCreateDate(LocalDateTime.now());


        // Kafka Template 을 사용하여 메세지를 지정된 토픽으로 전송
        try {
            if (message.getType().equals(MessageType.TALK)) {
                chatService.save(message);
                kafkaTemplate.send(KafkaConstants.KAFKA_AI_TOPIC, message);
                ChatMessageDto aiMessage = AiResponse(message);
                log.debug("ai message {}", aiMessage);
                kafkaTemplate.send(KafkaConstants.KAFKA_AI_TOPIC, aiMessage);
            }
            else if (message.getType().equals(MessageType.ENTER)) {
                message.setMessage(message.getSender() +"님 입장!!");
                chatService.save(message);
                kafkaTemplate.send(KafkaConstants.KAFKA_AI_TOPIC, message);
            }
            else if (message.getType().equals(MessageType.LEAVE)) {
                message.setMessage(message.getSender() +"님 퇴장!!");
                chatService.save(message);
                log.debug("user message {}", message);
                kafkaTemplate.send(KafkaConstants.KAFKA_AI_TOPIC, message);

            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private ChatMessageDto AiResponse(ChatMessageDto message) {
        String content = chatClient.prompt()
                .system(AITrainingScript.INFORMATION)
                .user(message.getMessage())
                .call()
                .content();

        return new ChatMessageDto(MessageType.TALK, message.getRoomId(),
                "AI", content, null,LocalDateTime.now(), null,null, true);

    }
}
