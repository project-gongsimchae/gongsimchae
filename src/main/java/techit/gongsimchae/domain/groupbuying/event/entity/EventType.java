package techit.gongsimchae.domain.groupbuying.event.entity;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum EventType {

    DISCOUNT("할인"), COUPON("쿠폰발급"), COUPON_CODE("쿠폰코드생성");

    String eventTypeName;

    public static EventType getInstanceByEventTypeName(String eventTypeName) {
        for (EventType value : values()) {
            if (value.getEventTypeName().equals(eventTypeName)){
                return value;
            }
        }
        throw new CustomWebException(ErrorMessage.EVENT_TYPE_NOT_VALID.getMessage());
    }
}
