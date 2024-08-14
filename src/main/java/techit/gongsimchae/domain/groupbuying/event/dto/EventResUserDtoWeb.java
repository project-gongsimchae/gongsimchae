package techit.gongsimchae.domain.groupbuying.event.dto;

import lombok.Data;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;
import techit.gongsimchae.domain.groupbuying.event.entity.EventType;

@Data
public class EventResUserDtoWeb {

    private EventType eventType;
    private String eventName;
    private Long eventId;
    private String eventBannerImage;

    public EventResUserDtoWeb(Event event, String eventBannerImage){
        this.eventType = event.getEventType();
        this.eventName = event.getEventName();
        this.eventId = event.getId();
        this.eventBannerImage = eventBannerImage;
    }

}
