package techit.gongsimchae.domain.portion.notifications.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import techit.gongsimchae.domain.portion.notifications.entity.Notifications;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {
    @Query("select n from Notifications n join fetch n.user u where u.id = :id order by n.createDate desc")
    List<Notifications> findAllUserId(@Param("id") Long userId);


    @Query("select count(n) from Notifications n join n.user u where u.id = :id and n.isRead = :read")
    Long countByUserChecked(@Param("id") Long id, @Param("read") int read);

    @Query("select n from Notifications n join n.user u where u.id = :id and n.isRead = 0")
    List<Notifications> findAllUnreadNotificationsByUser(Long id);
    @Query("select n from Notifications n where n.isRead > :number")
    Page<Notifications> findAllUnreadNotifications(@Param("number") Long isRead, Pageable pageable);
}
