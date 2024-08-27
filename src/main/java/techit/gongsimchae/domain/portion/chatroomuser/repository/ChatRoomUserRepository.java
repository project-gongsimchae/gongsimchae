package techit.gongsimchae.domain.portion.chatroomuser.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.portion.chatroom.entity.ChatRoom;
import techit.gongsimchae.domain.portion.chatroomuser.entity.ChatRoomUser;

import java.util.List;
import java.util.Optional;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

    @Query("select cru from ChatRoomUser cru join cru.user u join cru.chatRoom cr where u.loginId = :loginId and cr.roomId = :roomId")
    Optional<ChatRoomUser> findByChatUser(@Param("roomId") String roomId,@Param("loginId") String loginId);


    @Query("select cru from ChatRoomUser cru join fetch cru.user u join fetch cru.chatRoom cr where cr.roomId = :roomId and u.loginId in :loginIds ")
    List<ChatRoomUser> findAllByInactiveAndNotNotified(@Param("roomId") String roomId, @Param("loginIds") List<String> loginIds);

    @EntityGraph(attributePaths = {"user"})
    List<ChatRoomUser> findAllByChatRoomId(Long chatRoomId);

    void deleteByUserAndChatRoom(User user, ChatRoom chatRoom);
}
