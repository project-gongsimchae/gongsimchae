package techit.gongsimchae.domain.groupbuying.eventcategory.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.eventcategory.entity.EventCategory;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
    void deleteAllByEventId(Long eventId);

    List<EventCategory> findAllByEventId(Long eventId);
}
