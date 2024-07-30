package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techit.gongsimchae.domain.admin.coupon.service.CouponService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {


    @GetMapping
    public String adminDashboard() {
        return "admin/admin";
    }


}
