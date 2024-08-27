package techit.gongsimchae.domain.groupbuying.event.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;
import techit.gongsimchae.domain.groupbuying.event.entity.EventType;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByEventName(String eventName);
    List<Event> findByExpirationDateAfterAndEventStatusEqualsAndEventTypeNot(LocalDateTime localDateTime, Integer eventStatus, EventType eventType);
    List<Event> findAllByEventStatus(Integer eventStatus);
    List<Event> findAllByOrderByEventStatusAscExpirationDateAsc();
}
