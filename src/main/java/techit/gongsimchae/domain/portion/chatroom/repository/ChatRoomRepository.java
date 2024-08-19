package techit.gongsimchae.domain.portion.chatroom.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.portion.chatroom.entity.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @EntityGraph(attributePaths = {"subdivision"})
    Optional<ChatRoom> findByRoomId(String roomId);

    Optional<ChatRoom> findBySubdivisionId(Long subdivisionId);
}
