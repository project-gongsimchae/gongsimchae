package techit.gongsimchae.domain.groupbuying.eventcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.eventcategory.entity.EventCategory;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
    void deleteAllByEventId(Long eventId);
}
