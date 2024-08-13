package techit.gongsimchae.domain.groupbuying.event.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.imagefile.repository.ImageFileRepository;
import techit.gongsimchae.domain.common.imagefile.service.ImageS3Service;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.category.repository.CategoryRepository;
import techit.gongsimchae.domain.groupbuying.event.dto.EventCreateReqDtoWeb;
import techit.gongsimchae.domain.groupbuying.event.dto.EventResUserDtoWeb;
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
        List<Item> categoryItems = itemRepository.findAllByCategoryIn(categories);
        for (Item categoryItem : categoryItems) {
            categoryItem.updateDiscountRate(eventDto.getDiscountRate());
        }
        Event event = eventRepository.save(new Event(eventDto));
        for (Category category : categories) {
            eventCategoryRepository.save(new EventCategory(event, category));
        }
        imageS3Service.storeFile(eventDto.getEventBannerImage(), "gongsimchae/event-banner", event);
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
        imageS3Service.storeFile(eventDto.getEventBannerImage(), "gongsimchae/event-banner", event);
    }

    public void createCouponCodeEvent(EventCreateReqDtoWeb eventDto) {
        List<Category> categories = categoryRepository.findAllByNameIn(eventDto.getCategoryNames());
        Event event = eventRepository.save(new Event(eventDto));
        for (Category category : categories) {
            eventCategoryRepository.save(new EventCategory(event, category));
        }
    }

    public List<Event> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return eventRepository.findAll();
    }

    public List<EventResUserDtoWeb> getEvents(){
        List<Event> activatedEvents = eventRepository.findByExpirationDateAfter(LocalDateTime.now());
        List<EventResUserDtoWeb> eventResAdminDtoWebs = new ArrayList<>();
        for (Event activatedEvent : activatedEvents) {
            eventResAdminDtoWebs.add(new EventResUserDtoWeb(activatedEvent, imageFileRepository.findByEvent(activatedEvent).getStoreFilename()));
        }
        return eventResAdminDtoWebs;
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
