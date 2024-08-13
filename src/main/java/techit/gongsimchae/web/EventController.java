package techit.gongsimchae.web;

import static techit.gongsimchae.domain.groupbuying.event.entity.EventType.COUPON;
import static techit.gongsimchae.domain.groupbuying.event.entity.EventType.COUPON_CODE;
import static techit.gongsimchae.domain.groupbuying.event.entity.EventType.DISCOUNT;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import techit.gongsimchae.domain.common.imagefile.service.ImageS3Service;
import techit.gongsimchae.domain.groupbuying.coupon.service.CouponService;
import techit.gongsimchae.domain.groupbuying.couponuser.service.CouponUserService;
import techit.gongsimchae.domain.groupbuying.event.dto.EventCreateReqDtoWeb;
import techit.gongsimchae.domain.groupbuying.event.dto.EventResAdminDtoWeb;
import techit.gongsimchae.domain.groupbuying.event.dto.EventResUserDtoWeb;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;
import techit.gongsimchae.domain.groupbuying.event.entity.EventType;
import techit.gongsimchae.domain.groupbuying.event.service.EventService;
import techit.gongsimchae.domain.groupbuying.eventcategory.service.EventCategoryService;
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
    private final EventCategoryService eventCategoryService;
    private final ImageS3Service imageS3Service;
    private final CouponUserService couponUserService;


    //--------------------------------------------- event --------------------------------------------

    /**
     * 요청 : 네비게이션 바 - 이벤트 탭
     * 응답 : 이벤트 목록 페이지
     */
    @GetMapping("/event")
    public String getEventPage(Model model){
        List<EventResUserDtoWeb> eventResUserDtoWebs = eventService.getActivatedEvents();
        model.addAttribute("events", eventResUserDtoWebs);
        return "/category/event";
    }

    //--------------------------------------------- admin --------------------------------------------

    /**
     * 요청 : 관리자 페이지 - 이벤트 생성
     * 응답 : 이벤트 생성 폼
     */
    @GetMapping("/admin/event/create-form")
    public String showCreateForm(@ModelAttribute(name = "event") EventCreateReqDtoWeb eventCreateReqDtoWeb) {
        return "admin/event/eventForm";
    }

    /**
     * 요청 : 관리자 페이지 - 이벤트 관리
     * 응답 : 이벤트 목록
     */
    @GetMapping("/admin/event")
    public String getEventList(Model model){
        List<Event> events = eventService.getAllEvents();
        List<EventResAdminDtoWeb> eventResAdminDtoWebs = couponService.getCouponEventInfo(events);
        model.addAttribute("events", eventResAdminDtoWebs);
        return "admin/event/event";
    }

    /**
     * 요청 : 관리자 페이지 - 이벤트 생성 버튼
     * 응답 : redirect:/이벤트 관리 페이지
     */
    @PostMapping("/admin/event")
    public String createEvent(@ModelAttribute EventCreateReqDtoWeb eventDto){
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
            throw new CustomWebException(ErrorMessage.EVENT_TYPE_NOT_VALID);
        }
        return "redirect:/admin/event";
    }

    /**
     * 요청 : 관리자 페이지 - 이벤트 목록 - 이벤트 삭제
     * 응답 : redirect:/이벤트 관리 페이지
     */
    @Transactional
    @PostMapping("/admin/event/delete")
    public String deleteEvent(@RequestParam("event_id") Long eventId){
        /**
         * 1. imageFile에서 eventStatus가 1로 변경된 eventId를 가진 ImageFile 인스턴스의 ImageFileStatus를 1로 변경
         * 2. eventCategory에서 eventStatus가 1로 변경된 eventId를 가진 eventCategory 인스턴스의 eventCategoryStatus 를 1로 변경
         * 3. couponUser에서 couponStatus가 1로 변경된 couponId를 가진 couponUser의 couponUserStatus를 1로 변경
         * 4. coupon에서 eventStatus가 1로 변경된 eventId를 가진 coupon의 couponStatus를 1로 변경
         * 5. event의 eventStatus를 1로 변경
         */
        imageS3Service.deleteImageFile(eventId);
        eventCategoryService.deleteEventCategory(eventId);
        couponUserService.deleteCouponUser(eventId);
        couponService.deleteCoupon(eventId);
        eventService.deleteEvent(eventId);
        return "redirect:/admin/event";
    }
}
