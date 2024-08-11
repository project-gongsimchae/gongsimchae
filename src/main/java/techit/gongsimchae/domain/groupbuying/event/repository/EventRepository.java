package techit.gongsimchae.domain.groupbuying.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
