package techit.gongsimchae.domain.portion.chatroomuser.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.portion.chatroom.entity.ChatRoom;
import techit.gongsimchae.domain.portion.chatroomuser.entity.ChatRoomUser;

import java.util.List;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

    @Query("select cru from ChatRoomUser cru join fetch cru.user u join fetch cru.chatRoom cr where cr.roomId = :roomId and u.loginId in :loginIds ")
    List<ChatRoomUser> findAllByInactiveAndNotNotified(@Param("roomId") String roomId, @Param("loginIds") List<String> loginIds);

    @EntityGraph(attributePaths = {"user"})
    List<ChatRoomUser> findAllByChatRoomId(Long chatRoomId);

    @Modifying(clearAutomatically = true)
    @Query("delete from ChatRoomUser cru where cru.user.id = :userId and cru.chatRoom.id = :chatRoomId")
    void deleteByUserAndChatRoom(@Param("userId") Long userId, @Param("chatRoomId") Long chatRoomId);
}
