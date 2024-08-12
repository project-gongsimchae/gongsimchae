package techit.gongsimchae.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import techit.gongsimchae.domain.common.inquiry.dto.InquiryCreateDtoWeb;
import techit.gongsimchae.domain.common.inquiry.dto.InquiryRespDtoWeb;
import techit.gongsimchae.domain.common.inquiry.dto.InquiryUpdateReqDtoWeb;
import techit.gongsimchae.domain.common.inquiry.service.InquiryService;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.service.UserService;
import techit.gongsimchae.domain.common.wishlist.service.WishListService;
import techit.gongsimchae.domain.groupbuying.coupon.dto.CouponRespDtoWeb;
import techit.gongsimchae.domain.groupbuying.coupon.service.CouponService;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemRespDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.service.ItemService;
import techit.gongsimchae.domain.groupbuying.orders.dto.OrdersPaymentDto;
import techit.gongsimchae.domain.groupbuying.orders.entity.Orders;
import techit.gongsimchae.domain.groupbuying.orders.service.OrdersService;
import techit.gongsimchae.domain.portion.subdivision.service.SubdivisionService;
import techit.gongsimchae.global.dto.PrincipalDetails;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
@Slf4j
public class MyPageController {

    private final WishListService wishListService;
    private final SubdivisionService subdivisionService;
    private final UserService userService;
    private final CouponService couponService;
    private final InquiryService inquiryService;
    private final OrdersService ordersService;
    private final ItemService itemService;


    @GetMapping("/orders")
    public String orders(@AuthenticationPrincipal PrincipalDetails userDetails, Model model){
        String username = userDetails.getUsername();
        User user = userService.findByUserName(username);
        Long userId = user.getId();

        List<Orders> orders = ordersService.getUserOrders(userId);
        model.addAttribute("orders",orders);

        return "mypage/orders";
    }

    @GetMapping("/orders/{ordersId}")
    public String orderDetail(@PathVariable Long ordersId,
                              @AuthenticationPrincipal PrincipalDetails userDetails, Model model){
        String username = userDetails.getUsername();
        User user = userService.findByUserName(username);

        Orders ordersDetail = ordersService.getOrderDetail(ordersId,user.getId());
        OrdersPaymentDto ordersPayment = ordersService.getOrdersPayment(ordersId);
        if (ordersDetail == null) {
            return "redirect:error/4xx";
        }

        model.addAttribute("ordersDetail",ordersDetail);
        model.addAttribute("ordersPayment",ordersPayment);
        return "mypage/ordersDetail";
    }

    /**
     * 1:1 문의
     */

    /**
     * 내가 작성한 1:1문의 리스트
     */
    @GetMapping("/inquiry/list")
    public String inquiryList(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<InquiryRespDtoWeb> inquires = inquiryService.getInquiry(principalDetails);
        model.addAttribute("inquires", inquires);

        return "mypage/inquiryList";
    }

    /**
     * 1:1 문의 작성 폼
     */
    @GetMapping("/inquiry/form")
    public String inquiryForm(@ModelAttribute("inquiry") InquiryCreateDtoWeb dtoWeb, Model model,
                              @AuthenticationPrincipal PrincipalDetails principalDetails) {
        UserRespDtoWeb user = userService.getUser(principalDetails.getUsername());
        model.addAttribute("user", user);
        return "mypage/inquiryForm";
    }

    @PostMapping("/inquiry/form")
    public String inquires(@Valid @ModelAttribute("inquiry") InquiryCreateDtoWeb dtoWeb, BindingResult bindingResult,
                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if(bindingResult.hasErrors()) {
            log.debug("inquiry form error {}", bindingResult);
            return "mypage/inquiryForm";
        }
        inquiryService.createInquiry(dtoWeb, principalDetails);

        return "redirect:/mypage/inquiry/list";
    }

    /**
     * 내가 작성한 1:1 문의 보기
     * 문의 타입이랑, 내용만 볼 수 있음
     */
    @GetMapping("/inquiry/{id}")
    public String inquiryDetail(@PathVariable("id") String id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        InquiryRespDtoWeb inquiry = inquiryService.getInquiry(id);
        model.addAttribute("inquiry", inquiry);
        return "mypage/inquiryDetail";
    }

    /**
     * 1:1 문의 수정 폼
     */
    @GetMapping("/inquiry/update/{id}")
    public String updateInquiryForm(@PathVariable("id") String id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        InquiryRespDtoWeb inquiry = inquiryService.getInquiry(id);
        model.addAttribute("inquiry", inquiry);
        return "mypage/inquiryUpdate";
    }

    @PostMapping("/inquiry/update/{id}")
    public String updateInquiry(@PathVariable("id") String id, @Valid @ModelAttribute("inquiry") InquiryUpdateReqDtoWeb inquiryUpdateReqDtoWeb,
                                BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            log.debug("inquiry form error {}", bindingResult.getAllErrors());
            return "mypage/inquiryUpdate";
        }
        inquiryService.updateInquiry(id, inquiryUpdateReqDtoWeb);
        return "redirect:/mypage/inquiry/list";
    }

    /**
     * 1:1 문의 삭제
     */
    @PostMapping("/inquiry/delete/{id}")
    public String deleteInquiry(@PathVariable("id") String id) {
        inquiryService.deleteInquiry(id);
        log.debug("deleteInquiry {}", id);
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
    public String PickList(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        List<ItemRespDtoWeb> items = wishListService.getPickItems(principalDetails);
        log.debug("PickList {}", items);
        model.addAttribute("items", items);
        return "mypage/pickList";
    }

    @PostMapping("/pick/{itemId}")
    @ResponseBody
    public ResponseEntity<?> pickItem(@PathVariable("itemId") String itemId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        wishListService.pickItem(itemId, principalDetails);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pick/check/{itemId}")
    @ResponseBody
    public ResponseEntity<?> checkPick(@PathVariable("itemId") String itemId,@AuthenticationPrincipal PrincipalDetails principalDetails) {
        boolean result = wishListService.checkPickItem(itemId, principalDetails);
        return ResponseEntity.ok().body(result);
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

    /**
     * 참여 중인 소분글 목록
     */
    @GetMapping("/join")
    public String SubdivisionJoinList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                      Model model) {

        model.addAttribute("SubdivisionJoinRespDtoList",
                subdivisionService.findJoinSubdivisionByUserId(principalDetails.getAccountDto().getId()));

        return "mypage/subdivisionJoinList";
    }





    /**
     * 쿠폰
     */
    @GetMapping("/coupons")
    public String coupons(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        List<CouponRespDtoWeb> coupons = couponService.getUserCoupons(principalDetails);
        model.addAttribute("coupons", coupons);


        return "mypage/coupons";
    }

    @PostMapping("/coupons")
    public ResponseEntity<?> addCoupon(@RequestBody Map<String, String> payload, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        String couponCode = payload.get("code");
        if (!couponService.isValidCode(couponCode)) {
            log.debug("Invalid coupon code: {}", couponCode);
            return ResponseEntity.badRequest().body("유효하지 않는 쿠폰 코드입니다.");
        }
        userService.addCoupon(couponCode, principalDetails);
        return ResponseEntity.ok().build();
    }


}
