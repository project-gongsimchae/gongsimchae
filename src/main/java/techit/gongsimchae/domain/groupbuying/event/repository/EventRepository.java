package techit.gongsimchae.domain.groupbuying.event.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByEventName(String eventName);
    List<Event> findByExpirationDateAfterAndEventStatusEquals(LocalDateTime localDateTime, Integer eventStatus);

    List<Event> findAllByEventStatus(Integer eventStatus);
    List<Event> findAllByOrderByEventStatusAscExpirationDateAsc();
}
