package techit.gongsimchae.domain.groupbuying.event.service;

import static techit.gongsimchae.domain.groupbuying.event.entity.EventType.*;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.imagefile.service.ImageS3Service;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.category.repository.CategoryRepository;
import techit.gongsimchae.domain.groupbuying.coupon.entity.Coupon;
import techit.gongsimchae.domain.groupbuying.coupon.repository.CouponCustomRepository;
import techit.gongsimchae.domain.groupbuying.coupon.repository.CouponRepository;
import techit.gongsimchae.domain.groupbuying.couponcategory.entity.CouponCategory;
import techit.gongsimchae.domain.groupbuying.couponcategory.repository.CouponCategoryRepository;
import techit.gongsimchae.domain.groupbuying.event.dto.EventCreateReqDtoWeb;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;
import techit.gongsimchae.domain.groupbuying.event.entity.EventType;
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
public class EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final ItemRepository itemRepository;
    private final ImageS3Service imageS3Service;

    public void createDiscountEvent(EventCreateReqDtoWeb eventDto) {
        List<Category> categories = categoryRepository.findAllByNameIn(eventDto.getCategoryNames());
        List<Item> categoryItems = itemRepository.findAllByCategoryIn(categories);
        for (Item categoryItem : categoryItems) {
            categoryItem.updateDiscountRate(eventDto.getDiscountRate());
        }
        Event event = eventRepository.save(new Event(eventDto));
        for (Category category : categories) {
            eventCategoryRepository.save(new EventCategory(event, category));
        }
        imageS3Service.storeFile(eventDto.getEventBannerImage(), "gongsimchae/event-banner/", event);
    }

    public void createCouponEvent(EventCreateReqDtoWeb eventDto) {
        List<Category> categories = categoryRepository.findAllByNameIn(eventDto.getCategoryNames());
        Event event = eventRepository.save(new Event(eventDto));
        for (Category category : categories) {
            eventCategoryRepository.save(new EventCategory(event, category));
        }
        imageS3Service.storeFile(eventDto.getEventBannerImage(), "gongsimchae/event-banner/", event);
    }
}
