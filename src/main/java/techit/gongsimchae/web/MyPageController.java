package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techit.gongsimchae.domain.common.wishlist.service.WishListService;
import techit.gongsimchae.domain.portion.subdivision.service.SubdivisionService;
import techit.gongsimchae.global.dto.PrincipalDetails;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final WishListService wishListService;
    private final SubdivisionService subdivisionService;

    @GetMapping("/orders")
    public String mypage(){
        return "mypage/orders";
    }

    /**
     * 1:1 문의
     */
    @GetMapping("/inquiry/list")
    public String inquiryList(Model model) {

        return "mypage/inquiryList";
    }

    @GetMapping("/inquiry/form")
    public String inquiryForm() {

        return "mypage/inquiryForm";
    }

    @PostMapping("/inquiry/form")
    public String inquires(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        return "redirect:/mypage/inquiry/list";
    }

    /**
     * 상품 후기
     */
    @GetMapping("/reviews")
    public String reviews(Model model) {
        return "mypage/reviews";
    }

    @GetMapping("/reviews/write")
    public String reviewForm() {
        return "mypage/reviewForm";
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
        return "mypage/pickList";
    }

    /**
     * 소분 글 관심 목록
     */
    @GetMapping("/interest/list")
    public String InterestList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                               Model model) {

        model.addAttribute("subdivisionWishListRespDtoList",
                wishListService.getSubdivisionWishLists(principalDetails.getAccountDto().getId()));

        return "mypage/subdivisionWishlist";
    }

    /**
     * 내가 쓴 글 목록
     */
    @GetMapping("/write")
    public String mySubdivisionList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                    Model model) {

        model.addAttribute("mySubdivisionRespDtoList",
                subdivisionService.findSubdivisionByUserId(principalDetails.getAccountDto().getId()));

        return "mypage/mySubdivisionList";
    }
}
