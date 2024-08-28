package techit.gongsimchae.domain.groupbuying.event.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.imagefile.entity.S3VO;
import techit.gongsimchae.domain.common.imagefile.repository.ImageFileRepository;
import techit.gongsimchae.domain.common.imagefile.service.ImageS3Service;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.category.repository.CategoryRepository;
import techit.gongsimchae.domain.groupbuying.event.dto.EventCreateReqDtoWeb;
import techit.gongsimchae.domain.groupbuying.event.dto.EventResUserDtoWeb;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;
import techit.gongsimchae.domain.groupbuying.event.entity.EventType;
import techit.gongsimchae.domain.groupbuying.event.repository.EventRepository;
import techit.gongsimchae.domain.groupbuying.eventcategory.entity.EventCategory;
import techit.gongsimchae.domain.groupbuying.eventcategory.repository.EventCategoryRepository;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.item.repository.ItemRepository;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;
import techit.gongsimchae.global.util.EntityStatus;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final ItemRepository itemRepository;
    private final ImageS3Service imageS3Service;
    private final ImageFileRepository imageFileRepository;
    public void createDiscountEvent(EventCreateReqDtoWeb eventDto) {
        if (eventDto.getEventBannerImage().isEmpty()){
            throw new CustomWebException(ErrorMessage.EVENT_BANNER_IMAGE_EMPTY);
        }
        List<Category> categories = categoryRepository.findAllByNameIn(eventDto.getCategoryNames());
        List<Item> categoryItems = itemRepository.findAllByCategoryInAndDeleteStatus(categories, 0);
        for (Item categoryItem : categoryItems) {
            categoryItem.plusDiscountRate(eventDto.getDiscountRate());
        }
        Event event = eventRepository.save(new Event(eventDto));
        for (Category category : categories) {
            eventCategoryRepository.save(new EventCategory(event, category));
        }
        imageS3Service.storeFile(eventDto.getEventBannerImage(), S3VO.EVENT_BANNER_DIRECTORY, event);
    }

    public void createCouponEvent(EventCreateReqDtoWeb eventDto) {
        if (eventDto.getEventBannerImage().isEmpty()){
            throw new CustomWebException(ErrorMessage.EVENT_BANNER_IMAGE_EMPTY);
        }
        List<Category> categories = categoryRepository.findAllByNameIn(eventDto.getCategoryNames());
        Event event = eventRepository.save(new Event(eventDto));
        for (Category category : categories) {
            eventCategoryRepository.save(new EventCategory(event, category));
        }
        imageS3Service.storeFile(eventDto.getEventBannerImage(), S3VO.EVENT_BANNER_DIRECTORY, event);
    }

    public void createCouponCodeEvent(EventCreateReqDtoWeb eventDto) {
        List<Category> categories = categoryRepository.findAllByNameIn(eventDto.getCategoryNames());
        Event event = eventRepository.save(new Event(eventDto));
        for (Category category : categories) {
            eventCategoryRepository.save(new EventCategory(event, category));
        }
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAllByOrderByEventStatusAscExpirationDateAsc();
    }

    public List<EventResUserDtoWeb> getActivatedEvents(){
        List<Event> activatedEvents = eventRepository.findByExpirationDateAfterAndEventStatusEqualsAndEventTypeNot(LocalDateTime.now(),
                EntityStatus.EXIST, EventType.COUPON_CODE);
        List<EventResUserDtoWeb> eventResUserDtoWebs = new ArrayList<>();
        for (Event activatedEvent : activatedEvents) {
            List<EventCategory> eventCategories = eventCategoryRepository.findAllByEventId(activatedEvent.getId());
            List<Category> categories = eventCategories.stream()
                    .map((eventCategory) -> categoryRepository.findById(eventCategory.getId()).orElseThrow(
                            () -> new CustomWebException(ErrorMessage.CATEGORY_NOT_FOUND)
                    )).toList();
            eventResUserDtoWebs.add(new EventResUserDtoWeb(activatedEvent, imageFileRepository.findByEvent(activatedEvent).getStoreFilename(), categories));
        }
        return eventResUserDtoWebs;
    }

    public void deleteEvent(Long eventId) {
        /**
         * 1. event의 eventStatus를 1로 변경
         * 2. eventCategory에서 eventStatus가 1로 변경된 eventId를 가진 eventCategory 인스턴스의 eventCategoryStatus 를 1로 변경
         * 3. coupon에서 eventStatus가 1로 변경된 eventId를 가진 coupon의 couponStatus를 1로 변경
         * 4. couponUser에서 couponStatus가 1로 변경된 couponId를 가진 couponUser의 couponUserStatus를 1로 변경
         * 5. imageFile에서 eventStatus가 1로 변경된 eventId를 가진 ImageFile 인스턴스의 ImageFileStatus를 1로 변경
         */
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new CustomWebException(ErrorMessage.EVENT_NOT_FOUND)
        );
        event.setStatusDeleted();


    }
}
