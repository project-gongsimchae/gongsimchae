package techit.gongsimchae.domain.groupbuying.event.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;
import techit.gongsimchae.domain.groupbuying.event.entity.EventType;

@Data
@NoArgsConstructor
public class EventResAdminDtoWeb {

    private EventType eventType;
    private String eventName;
    private Integer discountRate;
    private Integer maxDiscount;
    private LocalDateTime expirationDate;
    private Long eventId;
    private String couponCode;
    private Integer eventStatus;

    public EventResAdminDtoWeb(Event event, String couponCode){
        this.eventType = event.getEventType();
        this.eventName = event.getEventName();
        this.discountRate = event.getDiscountRate();
        this.maxDiscount = event.getMaxDiscount();
        this.expirationDate = event.getExpirationDate();
        this.eventId = event.getId();
        this.couponCode = couponCode;
        this.eventStatus = event.getEventStatus();
    }
}
