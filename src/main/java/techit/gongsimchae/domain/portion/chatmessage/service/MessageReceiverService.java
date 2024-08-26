package techit.gongsimchae.domain.portion.chatmessage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.portion.chatmessage.dto.ChatMessageDto;
import techit.gongsimchae.global.config.kafka.KafkaConstants;

@Service
@RequiredArgsConstructor
public class MessageReceiverService {

    private final SimpMessageSendingOperations template;

    @KafkaListener(topics = KafkaConstants.KAFKA_TOPIC, groupId = KafkaConstants.GROUP_ID)
    public void receiveMessage(ChatMessageDto message) {
        try {

            // 메세지객체 내부의 채팅방 ID 참조 -> 구독자에게 메세지 발송
            template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = KafkaConstants.KAFKA_AI_TOPIC, groupId = KafkaConstants.GROUP_ID)
    public void receiveAIMessage(ChatMessageDto message) {
        try {
            // 메세지객체 내부의 채팅방 ID 참조 -> 구독자에게 메세지 발송
            template.convertAndSend("/sub/chat/ai/room/" + message.getRoomId(), message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
