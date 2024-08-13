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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import techit.gongsimchae.domain.groupbuying.coupon.service.CouponService;
import techit.gongsimchae.domain.groupbuying.event.dto.EventCreateReqDtoWeb;
import techit.gongsimchae.domain.groupbuying.event.dto.EventResAdminDtoWeb;
import techit.gongsimchae.domain.groupbuying.event.dto.EventResUserDtoWeb;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;
import techit.gongsimchae.domain.groupbuying.event.entity.EventType;
import techit.gongsimchae.domain.groupbuying.event.service.EventService;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

/**
 * Description
 * event/로 시작 시 사용자의 이벤트 관련 기능
 * admin/으로 시작 시 관리자의 이벤트 관련 기능
 * 구분선을 기준으로 상단에는 event/ 하단에는 admin/ 기능을 추가했습니다.
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final CouponService couponService;

    //--------------------------------------------- event --------------------------------------------

    /**
     * 요청 : 네비게이션 바 - 이벤트 탭
     * 응답 : 이벤트 목록 페이지
     */
    @GetMapping("/event")
    public String getEventPage(Model model){
        List<EventResUserDtoWeb> eventResUserDtoWebs = eventService.getEvents();
        model.addAttribute("events", eventResUserDtoWebs);
        return "/category/event";
    }

    //--------------------------------------------- admin --------------------------------------------

    /**
     * 요청 : 관리자 페이지 - 이벤트 생성
     * 응답 : 이벤트 생성 폼
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
     * 요청 : 관리자 페이지 - 이벤트 관리
     * 응답 : 이벤트 목록
     */
    @GetMapping("/admin/event")
    public String getEventList(Model model){
        List<Event> events = eventService.getAllEvents();
        List<EventResAdminDtoWeb> eventResAdminDtoWebs = couponService.getEventInfo(events);
        model.addAttribute("events", eventResAdminDtoWebs);
        return "admin/event/event";
    }

    /**
     * 요청 : 관리자 페이지 - 이벤트 생성 버튼
     * 응답 : redirect:/이벤트 관리 페이지
     */
    @PostMapping("/admin/event")
    public String createEvent(EventCreateReqDtoWeb eventDto){
        EventType eventType = EventType.getInstanceByEventTypeName(eventDto.getEventTypeName());
        if (eventType.equals(DISCOUNT)) {
            eventService.createDiscountEvent(eventDto);
        } else if (eventType.equals(COUPON)) {
            eventService.createCouponEvent(eventDto);
            couponService.createEventCoupon(eventDto);
        } else if (eventType.equals(COUPON_CODE)) {
            eventService.createCouponCodeEvent(eventDto);
            couponService.createEventCoupon(eventDto);
        } else {
            throw new CustomWebException(ErrorMessage.EVENT_TYPE_NOT_VALID.getMessage());
        }
        return "redirect:/admin/event";
    }

    /**
     * 요청 : 관리자 페이지 - 이벤트 목록 - 이벤트 삭제
     * 응답 : redirect:/이벤트 관리 페이지
     */
    @PostMapping("/admin/event/delete")
    public String deleteEvent(@RequestParam("event_id") Long eventId){

        return "redirect:/admin/event";
    }
}
