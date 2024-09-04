package techit.gongsimchae.domain.groupbuying.eventcategory.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;
import techit.gongsimchae.domain.groupbuying.eventcategory.entity.EventCategory;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
    void deleteAllByEventId(Long eventId);
    List<EventCategory> findAllByEvent(Event event);

    List<EventCategory> findAllByEventId(Long eventId);

    Optional<EventCategory> findByCategoryId(Long categoryId);
}
