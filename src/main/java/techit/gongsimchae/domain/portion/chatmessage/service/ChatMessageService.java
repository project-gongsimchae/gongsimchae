package techit.gongsimchae.domain.portion.chatmessage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.portion.chatmessage.dto.ChatMessageDto;
import techit.gongsimchae.domain.portion.chatmessage.entity.ChatMessage;
import techit.gongsimchae.domain.portion.chatmessage.repository.ChatMessageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void save(ChatMessageDto chatMessageDto) {
        ChatMessage chatMessage = new ChatMessage(chatMessageDto);
        chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessageDto> getMessages(String roomId) {
        return chatMessageRepository.findAllByRoomId(roomId).stream()
                .map(ChatMessageDto::new).collect(Collectors.toList());
    }
}
