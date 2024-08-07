package techit.gongsimchae.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import techit.gongsimchae.domain.groupbuying.coupon.dto.CouponCreateReqDtoWeb;
import techit.gongsimchae.domain.groupbuying.coupon.dto.CouponRespDtoWeb;
import techit.gongsimchae.domain.groupbuying.coupon.service.CouponService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/event")
public class CouponController {

    private final CouponService couponService;

    @GetMapping
    public String listCoupons(Model model) {
        List<CouponRespDtoWeb> coupons = couponService.getAllCoupons();
        model.addAttribute("coupons", coupons);
        return "admin/event/event"; // 이미 구현된 쿠폰 목록 페이지
    }

    @GetMapping("/create")
    public String showCreateForm(@ModelAttribute("coupon")CouponCreateReqDtoWeb coupon) {
        return "admin/event/eventForm";
    }


    @PostMapping("/create")
    public String createCoupon(@Valid  @ModelAttribute("coupon")CouponCreateReqDtoWeb coupon, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "admin/event/eventForm";
        }

        couponService.saveCoupon(coupon);
        return "redirect:/admin/event"; // 쿠폰 생성 후 쿠폰 목록 페이지로 리다이렉트
    }

    @PostMapping("/delete")
    public String deleteCoupon(@RequestParam Long id) {
        couponService.deleteCoupon(id);
        return "redirect:/admin/event";
    }




}



