package techit.gongsimchae.domain.portion.notifications.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import techit.gongsimchae.domain.portion.notifications.entity.Notifications;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {
    @Query("select n from Notifications n join fetch n.user u where u.id = :id")
    List<Notifications> findAllUserId(@Param("id") Long userId);

    /*@Query("select count(n) from Notifications n join fetch n.user u where u.id = :id and n.isRead = :read")
    Long countByUserChecked(@Param("id") Long id, @Param("read") int read);*/
}
