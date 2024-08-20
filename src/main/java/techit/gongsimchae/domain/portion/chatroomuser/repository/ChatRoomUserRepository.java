package techit.gongsimchae.domain.portion.chatroomuser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import techit.gongsimchae.domain.portion.chatroomuser.entity.ChatRoomUser;

import java.util.Optional;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
    Optional<ChatRoomUser> findByUserIdAndChatRoomId(Long userId, Long chatRoomId);

    @Query("select cru from ChatRoomUser cru join cru.user u join cru.chatRoom cr where u.nickname = :nickname and cr.roomId = :roomId")
    Optional<ChatRoomUser> findByChatUser(@Param("roomId") String roomId,@Param("nickname") String nickname);
}
