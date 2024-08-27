package techit.gongsimchae.domain.groupbuying.event.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;
import techit.gongsimchae.domain.groupbuying.event.entity.EventType;

@Data
public class EventResUserDtoWeb {

    private EventType eventType;
    private String eventName;
    private Long eventId;
    private String eventBannerImage;
    private List<Category> eventCategories;

    public EventResUserDtoWeb(Event event, String eventBannerImage, List<Category> eventCategories){
        this.eventType = event.getEventType();
        this.eventName = event.getEventName();
        this.eventId = event.getId();
        this.eventBannerImage = eventBannerImage;
        this.eventCategories = eventCategories;
    }

}
