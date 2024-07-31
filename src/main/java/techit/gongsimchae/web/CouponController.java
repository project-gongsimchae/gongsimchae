package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techit.gongsimchae.domain.admin.coupon.entity.Coupon;
import techit.gongsimchae.domain.admin.coupon.service.CouponService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/event")
public class CouponController {

    private final CouponService couponService;

    @GetMapping
    public String listCoupons(Model model) {
        model.addAttribute("coupons", couponService.getAllCoupons());
        return "admin/event/event"; // 이미 구현된 쿠폰 목록 페이지
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("coupon", new Coupon());
        return "admin/event/eventForm";
    }


    @PostMapping("/create")
    public String createCoupon(@ModelAttribute Coupon coupon) {
        couponService.saveCoupon(coupon);
        return "redirect:/admin/event"; // 쿠폰 생성 후 쿠폰 목록 페이지로 리다이렉트
    }

    @PostMapping("/delete")
    public String deleteCoupon(@RequestParam Long id) {
        couponService.deleteCoupon(id);
        return "redirect:/admin/event";
    }




}



