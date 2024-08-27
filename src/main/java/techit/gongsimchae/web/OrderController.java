package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techit.gongsimchae.domain.common.user.service.UserService;
import techit.gongsimchae.domain.groupbuying.cart.service.CartService;
import techit.gongsimchae.domain.groupbuying.orderitem.service.OrderItemService;
import techit.gongsimchae.domain.groupbuying.orders.dto.TempOrderItemDto;
import techit.gongsimchae.domain.groupbuying.orders.dto.TempUserDeliveryDto;
import techit.gongsimchae.domain.groupbuying.orders.service.OrdersService;
import techit.gongsimchae.global.dto.PrincipalDetails;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrdersService ordersService;

    /*@GetMapping("/주문하기")
    아이템옵션을 받아서 -> 카트에서 -> 아이템 오더 -> 오더
     */

    @PostMapping("/temp")
    public String createTempOrder(@AuthenticationPrincipal PrincipalDetails userDetail,
                                  @RequestParam("selectedItemOptionId") List<Long> selectedItemOptionIds,
                                  RedirectAttributes redirectAttributes) {
        Long userId = userDetail.getAccountDto().getId();
        List<TempOrderItemDto> tempOrderItems = ordersService.createTempOrder(userId, selectedItemOptionIds);
        int totalPrice = ordersService.amountPrice(tempOrderItems);
        int totalDiscountPrice = tempOrderItems.stream()
                        .mapToInt(item -> item.getOriginalPrice()*item.getQuantity() - item.getPrice() * item.getQuantity())
                        .sum();

        redirectAttributes.addFlashAttribute("tempOrderItems", tempOrderItems);
        redirectAttributes.addFlashAttribute("totalPrice", totalPrice);
        redirectAttributes.addFlashAttribute("totalDiscountPrice", totalDiscountPrice);
        redirectAttributes.addFlashAttribute("paymentMethod", List.of("토스페이","카카오페이"));

        return "redirect:/order/confirm";
    }

    @GetMapping("/confirm")
    public String showOrderConfirmation(@AuthenticationPrincipal PrincipalDetails userDetail,
                                        @ModelAttribute("tempOrderItems") List<TempOrderItemDto> tempOrderItems,
                                        @ModelAttribute("totalPrice") Integer totalPrice,
                                        @ModelAttribute("totalDiscountPrice") Integer totalDiscountPrice,
                                        @ModelAttribute("paymentMethod") List<String> paymentMethod,
                                        Model model) {
        TempUserDeliveryDto userDeliveryInfo = ordersService.createTempUserDeliveryInfo(userDetail.getAccountDto().getId());

        model.addAttribute("userDeliveryInfo",userDeliveryInfo);
        return "groupbuying/order";
    }
}
