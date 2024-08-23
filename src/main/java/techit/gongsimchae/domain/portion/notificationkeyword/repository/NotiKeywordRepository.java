package techit.gongsimchae.domain.portion.notificationkeyword.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import techit.gongsimchae.domain.portion.notificationkeyword.entity.NotificationKeyword;

import java.util.List;

public interface NotiKeywordRepository extends JpaRepository<NotificationKeyword, Long> {
    List<NotificationKeyword> findAllByUserId(Long id);

    @Modifying
    @Query("delete from NotificationKeyword nk where nk.keyword = :keyword and nk.user.id = :id")
    void deleteKeywordByUser(@Param("keyword") String keyword, @Param("id") Long id);
}
