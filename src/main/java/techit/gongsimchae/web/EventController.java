package techit.gongsimchae.web;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techit.gongsimchae.domain.groupbuying.coupon.dto.CouponRespDtoWeb;
import techit.gongsimchae.domain.groupbuying.coupon.service.CouponService;

@Controller
@RequiredArgsConstructor
public class EventController {

    private final CouponService couponService;

    @GetMapping("/event")
    public String getEventPageVer3(Model model){
        List<CouponRespDtoWeb> events = couponService.getAllCoupons();
        model.addAttribute("events", events);
        return "/category/event";
    }
}
