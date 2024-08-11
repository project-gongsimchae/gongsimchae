package techit.gongsimchae.web;

import static techit.gongsimchae.domain.groupbuying.event.entity.EventType.COUPON;
import static techit.gongsimchae.domain.groupbuying.event.entity.EventType.COUPON_CODE;
import static techit.gongsimchae.domain.groupbuying.event.entity.EventType.DISCOUNT;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import techit.gongsimchae.domain.groupbuying.coupon.dto.CouponRespDtoWeb;
import techit.gongsimchae.domain.groupbuying.coupon.service.CouponService;
import techit.gongsimchae.domain.groupbuying.event.dto.EventCreateReqDtoWeb;
import techit.gongsimchae.domain.groupbuying.event.entity.EventType;
import techit.gongsimchae.domain.groupbuying.event.service.EventService;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

@Controller
@Slf4j
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final CouponService couponService;

    /**
     * 이벤트 페이지 반환
     */
    @GetMapping("/event")
    public String getEventPage(Model model){
        List<CouponRespDtoWeb> events = couponService.getAllCoupons();
        model.addAttribute("events", events);
        return "/category/event";
    }

    /**
     * 이벤트 생성 폼 반환
     */
    @GetMapping("/admin/event/create-form")
    public String showCreateForm(Model model) {
        EventType[] values = EventType.values();
        List<String> eventTypeNames = new ArrayList<>();
        for (EventType value : values) {
            eventTypeNames.add(value.getEventTypeName());
        }
        model.addAttribute("eventTypes", eventTypeNames);
        model.addAttribute("event", new EventCreateReqDtoWeb());
        return "admin/event/eventForm";
    }

    /**
     * 이벤트 생성 메서드
     */
    @PostMapping("/event")
    public String createEvent(EventCreateReqDtoWeb eventDto){
        EventType eventType = EventType.getInstanceByEventTypeName(eventDto.getEventTypeName());
        if (eventType.equals(DISCOUNT)) {
            eventService.createDiscountEvent(eventDto);
        } else if (eventType.equals(COUPON)) {
            eventService.createCouponEvent(eventDto);
            couponService.createEventCoupon(eventDto);
        } else if (eventType.equals(COUPON_CODE)) {
            couponService.createEventCoupon(eventDto);
        } else {
            throw new CustomWebException(ErrorMessage.EVENT_TYPE_NOT_VALID.getMessage());
        }

        return "redirect:/admin/event/";
    }
}
