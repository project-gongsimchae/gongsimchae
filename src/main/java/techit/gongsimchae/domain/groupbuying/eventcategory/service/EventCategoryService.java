package techit.gongsimchae.domain.groupbuying.eventcategory.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.category.repository.CategoryRepository;
import techit.gongsimchae.domain.groupbuying.eventcategory.entity.EventCategory;
import techit.gongsimchae.domain.groupbuying.eventcategory.repository.EventCategoryRepository;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

@Service
@Transactional
@RequiredArgsConstructor
public class EventCategoryService {
    private final EventCategoryRepository eventCategoryRepository;
    private final CategoryRepository categoryRepository;

    public void deleteEventCategory(Long eventId) {
        List<EventCategory> eventCategories = eventCategoryRepository.findAllByEventId(eventId);
        eventCategories.forEach(EventCategory::setStatusDeleted);
    }

    public List<Category> getCategoriesByEvent(Long eventId) {
        List<EventCategory> eventCategories = eventCategoryRepository.findAllByEventId(eventId);
        return eventCategories.stream()
                .map((eventCategory -> categoryRepository.findById(eventCategory.getCategory().getId()).orElseThrow(
                        () -> new CustomWebException(ErrorMessage.CATEGORY_NOT_FOUND)
                ))).toList();
    }
}
