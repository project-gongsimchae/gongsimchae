package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.service.UserService;
import techit.gongsimchae.domain.groupbuying.orders.dto.OrdersPaymentDto;
import techit.gongsimchae.domain.groupbuying.orders.entity.Orders;
import techit.gongsimchae.domain.groupbuying.orders.service.OrdersService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {
    private final UserService userService;
    private final OrdersService ordersService;
    @GetMapping("/orders")
    public String orders(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String username = userDetails.getUsername();
        User user = userService.findByUserName(username);
        Long userId = user.getId();

        List<Orders> orders = ordersService.getUserOrders(userId);
        model.addAttribute("orders",orders);

        return "mypage/orders";
    }
    @GetMapping("/orders/{ordersId}")
    public String orderDetail(@PathVariable Long ordersId,
                              @AuthenticationPrincipal UserDetails userDetails, Model model){
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
}
