package techit.gongsimchae.domain.groupbuying.category.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.groupbuying.category.dto.CategoryReqDtoWeb;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.category.repository.CategoryRepository;
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

    public List<Category> getAllRemainingCategories(){
        return categoryRepository.findAllByCategoryStatus(0);
    }

    // 카테고리 ID로 조회
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void endCategoryEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new CustomWebException(ErrorMessage.EVENT_NOT_FOUND)
        );
        List<EventCategory> eventCategories = eventCategoryRepository.findAllByEvent(event);
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
        return categoryRepository.findAllByCategoryStatus(pageable, 0);
    }

    public void createCategory(CategoryReqDtoWeb categoryReqDtoWeb) {
        categoryRepository.save(new Category(categoryReqDtoWeb.getCategoryName()));
    }

    public void deleteCategory(CategoryReqDtoWeb categoryDtoWeb) {
        Category category = categoryRepository.findById(categoryDtoWeb.getCategoryId()).orElseThrow(
                () -> new CustomWebException(ErrorMessage.CATEGORY_NOT_FOUND)
        );
        category.delete();
    }
}
