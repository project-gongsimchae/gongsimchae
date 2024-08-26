package techit.gongsimchae.domain.groupbuying.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.groupbuying.category.dto.CategoryCreateDtoWeb;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.category.repository.CategoryRepository;

import java.util.List;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;
import techit.gongsimchae.domain.groupbuying.event.repository.EventRepository;
import techit.gongsimchae.domain.groupbuying.eventcategory.entity.EventCategory;
import techit.gongsimchae.domain.groupbuying.eventcategory.repository.EventCategoryRepository;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.item.repository.ItemRepository;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final ItemRepository itemRepository;
    private final EventRepository eventRepository;

    // 모든 카테고리 조회
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 카테고리 ID로 조회
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void endCategoryEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new CustomWebException(ErrorMessage.EVENT_NOT_FOUND)
        );
        List<EventCategory> eventCategories = eventCategoryRepository.findAllByEventId(eventId);
        for (EventCategory eventCategory : eventCategories) {
            Category category = eventCategory.getCategory();
            List<Item> items = itemRepository.findAllByCategory(category);
            items.forEach((element) -> element.minusDiscountRate(event.getDiscountRate()));
        }
    }

    /**
     * 카테고리 페이지네이션 조회
     */
    public Page<Category> getCategories(Pageable pageable){
        return categoryRepository.findAll(pageable);
    }

    public void createCategory(CategoryCreateDtoWeb categoryCreateDtoWeb) {
        categoryRepository.save(new Category(categoryCreateDtoWeb.getCategoryName()));
    }
}
