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
        List<Category> categories = categoryRepository.findAllByNameIn(eventDto.getCategoryNames());
        Event event = eventRepository.save(new Event(eventDto));
        for (Category category : categories) {
            eventCategoryRepository.save(new EventCategory(event, category));
        }
        imageS3Service.storeFile(eventDto.getEventBannerImage(), "gongsimchae/event-banner", event);
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
        eventRepository.deleteById(eventId);
    }
}
