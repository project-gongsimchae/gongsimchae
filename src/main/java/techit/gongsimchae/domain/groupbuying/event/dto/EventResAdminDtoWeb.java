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
    private Integer discountAmount;
    private Integer maxDiscount;
    private LocalDateTime expirationDate;
    private Long eventId;
    private String couponCode;

    public EventResAdminDtoWeb(Event event, String couponCode){
        this.eventType = event.getEventType();
        this.eventName = event.getEventName();
        this.discountRate = event.getDiscountRate();
        this.discountAmount = event.getDiscountAmount();
        this.maxDiscount = event.getMaxDiscount();
        this.expirationDate = event.getExpirationDate();
        this.eventId = event.getId();
        this.couponCode = couponCode;
    }
    /**
     *                 <th>이벤트 종류</th>
     *                 <th>이벤트명</th>
     *                 <th>할인율</th>
     *                 <th>할인금액</th>
     *                 <th>최대할인금액</th>
     *                 <th>만료기한</th>
     *                 <th>쿠폰코드</th>
     *                 <th>삭제</th>
     */
}
