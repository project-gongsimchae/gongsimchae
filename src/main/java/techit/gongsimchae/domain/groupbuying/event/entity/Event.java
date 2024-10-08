package techit.gongsimchae.domain.groupbuying.event.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.groupbuying.event.dto.EventCreateReqDtoWeb;

@Getter
@Entity
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EventType eventType;
    @Column(unique = true)
    private String eventName;
    private Integer discountRate;
    private Integer maxDiscount;
    private LocalDateTime expirationDate;
    private Integer eventStatus;

    public Event(EventCreateReqDtoWeb dto){
        this.eventType = EventType.getInstanceByEventTypeName(dto.getEventTypeName());
        this.eventName = dto.getEventName();
        this.discountRate = dto.getDiscountRate();
        this.maxDiscount = dto.getMaxDiscount();
        this.expirationDate = dto.getExpirationDate();
        this.eventStatus = 0;
    }

    public void setStatusDeleted() {
        this.eventStatus = 1;
    }
}
