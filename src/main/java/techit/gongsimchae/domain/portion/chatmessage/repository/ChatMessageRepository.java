package techit.gongsimchae.domain.portion.chatmessage.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import techit.gongsimchae.domain.portion.chatmessage.entity.ChatMessage;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findAllByRoomId(String roomId);
}
