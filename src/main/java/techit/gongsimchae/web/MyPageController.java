package techit.gongsimchae.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techit.gongsimchae.global.dto.PrincipalDetails;

@Controller
@RequestMapping("/mypage")
public class MyPageController {
    @GetMapping("/orders")
    public String mypage(){
        return "mypage/orders";
    }

    /**
     * 1:1 문의
     */
    @GetMapping("/inquiry/list")
    public String inquiryList(Model model) {

        return "user/inquiryList";
    }

    @GetMapping("/inquiry/form")
    public String inquiryForm() {

        return "user/inquiryForm";
    }

    @PostMapping("/inquiry/form")
    public String inquires(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        return "redirect:/inquiry/list";
    }

    /**
     * 상품 후기
     */
    @GetMapping("/reviews")
    public String reviews(Model model) {
        return "user/reviews";
    }

    @GetMapping("/reviews/write")
    public String reviewForm() {
        return "user/reviewForm";
    }

    @PostMapping("/reviews/write")
    public String viewsWrite(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return "redirect:/mypage/reviews";
    }

    /**
     * 찜
     */

    @GetMapping("/pick/list")
    public String PickList() {
        return "user/pickList";
    }
    /**
     * 관심 목록
     */

    @GetMapping("/interest/list")
    public String InterestList() {
        return "mypage/interestList";
    }
}
