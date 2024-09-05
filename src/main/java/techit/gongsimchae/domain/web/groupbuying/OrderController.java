package techit.gongsimchae.domain.web.groupbuying;

import com.siot.IamportRestClient.exception.IamportResponseException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techit.gongsimchae.domain.groupbuying.coupon.dto.CouponRespDtoWeb;
import techit.gongsimchae.domain.groupbuying.coupon.service.CouponService;
import techit.gongsimchae.domain.groupbuying.orders.dto.CancelOrderRequest;
import techit.gongsimchae.domain.groupbuying.orders.dto.OrderCreateRequestDto;
import techit.gongsimchae.domain.groupbuying.orders.dto.TempOrderItemDto;
import techit.gongsimchae.domain.groupbuying.orders.dto.TempUserDeliveryDto;
import techit.gongsimchae.domain.groupbuying.orders.service.OrdersService;
import techit.gongsimchae.domain.groupbuying.payment.dto.PaymentDto;
import techit.gongsimchae.domain.groupbuying.payment.service.PaymentsService;
import techit.gongsimchae.global.dto.PrincipalDetails;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
@Slf4j
public class OrderController {
    private final OrdersService ordersService;
    private final PaymentsService paymentsService;
    private final CouponService couponService;

    @PostMapping("/temp")
    public String createTempOrder(@AuthenticationPrincipal PrincipalDetails userDetail,
                                  @RequestParam("selectedItemOptionId") List<Long> selectedItemOptionIds,
                                  HttpSession session) {
        Long userId = userDetail.getAccountDto().getId();
        List<TempOrderItemDto> tempOrderItems = ordersService.createTempOrder(userId, selectedItemOptionIds);
        int totalPrice = ordersService.amountPrice(tempOrderItems);
        int totalDiscountPrice = tempOrderItems.stream()
                        .mapToInt(item -> item.getOriginalPrice()*item.getQuantity() - item.getPrice() * item.getQuantity())
                        .sum();

        List<CouponRespDtoWeb> couponRespDtoWebs = couponService.getUserCoupons(userDetail);


        session.setAttribute("couponRespDtoWebs",couponRespDtoWebs);
        session.setAttribute("tempOrderItems", tempOrderItems);
        session.setAttribute("totalPrice", totalPrice);
        session.setAttribute("totalDiscountPrice", totalDiscountPrice);

        return "redirect:/order/confirm";
    }

    @GetMapping("/confirm")
    public String showOrderConfirmation(@AuthenticationPrincipal PrincipalDetails userDetail,
                                        HttpSession session,
                                        Model model) {

        List<TempOrderItemDto> tempOrderItems = (List<TempOrderItemDto>) session.getAttribute("tempOrderItems");
        Integer totalPrice = (Integer) session.getAttribute("totalPrice");
        Integer totalDiscountPrice = (Integer) session.getAttribute("totalDiscountPrice");
        List<CouponRespDtoWeb> couponRespDtoWebs = (List<CouponRespDtoWeb>) session.getAttribute("couponRespDtoWebs");

        TempUserDeliveryDto userDeliveryInfo = ordersService.createTempUserDeliveryInfo(userDetail.getAccountDto().getId());
        List<PaymentDto> paymentMethod = paymentsService.pgProvider();

        model.addAttribute("couponRespDtoWebs",couponRespDtoWebs);
        model.addAttribute("paymentMethod",paymentMethod);
        model.addAttribute("userDeliveryInfo",userDeliveryInfo);
        model.addAttribute("tempOrderItems", tempOrderItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalDiscountPrice", totalDiscountPrice);
        return "groupbuying/order";
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> createOrder(@AuthenticationPrincipal PrincipalDetails userDetails,
                                              @RequestBody OrderCreateRequestDto requestDto,
                                              HttpSession session){
        Long userId = userDetails.getAccountDto().getId();
        List<TempOrderItemDto> tempOrderItems = (List<TempOrderItemDto>) session.getAttribute("tempOrderItems");

        if (tempOrderItems == null || tempOrderItems.isEmpty()) {
            return ResponseEntity.badRequest().body("주문 정보가 없습니다.");
        }

        String merchantUid = paymentsService.createOrder(userId, tempOrderItems, requestDto);
        session.setAttribute("merchantUid",merchantUid);
        return ResponseEntity.ok(merchantUid);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelOrder(@RequestBody CancelOrderRequest cancelOrderRequest){
        try {
            paymentsService.cancelOrder(cancelOrderRequest.getImpUid(),cancelOrderRequest.getMerchantUid());
        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok("취소완료");
    }
}
