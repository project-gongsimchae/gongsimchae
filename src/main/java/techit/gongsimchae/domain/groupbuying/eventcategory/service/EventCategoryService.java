package techit.gongsimchae.domain.groupbuying.eventcategory.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.groupbuying.eventcategory.entity.EventCategory;
import techit.gongsimchae.domain.groupbuying.eventcategory.repository.EventCategoryRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class EventCategoryService {

    private final EventCategoryRepository eventCategoryRepository;

    public void deleteEventCategory(Long eventId) {
        List<EventCategory> eventCategories = eventCategoryRepository.findAllByEventId(eventId);
        eventCategories.forEach(EventCategory::setStatusDeleted);
    }
}
