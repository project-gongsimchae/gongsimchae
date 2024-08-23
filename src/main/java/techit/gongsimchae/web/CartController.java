package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techit.gongsimchae.domain.groupbuying.cart.dto.CartAddRequest;
import techit.gongsimchae.domain.groupbuying.cart.dto.CartItemDto;
import techit.gongsimchae.domain.groupbuying.cart.dto.CartPriceInfoDto;
import techit.gongsimchae.domain.groupbuying.cart.service.CartService;
import techit.gongsimchae.global.dto.PrincipalDetails;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Boolean>> addToCart(@AuthenticationPrincipal PrincipalDetails userDetails,
                                          @RequestBody CartAddRequest request) {
        Long userId = userDetails.getAccountDto().getId();
        cartService.addToCart(userId, request.getCartItems());
        return ResponseEntity.ok(Map.of("success",true));
    }

    @PostMapping("/update/{itemOptionId}")
    public ResponseEntity<Map<String, Object>> updateCartItem(@AuthenticationPrincipal PrincipalDetails userDetails,
                                                              @PathVariable Long itemOptionId,
                                                              @RequestBody Map<String, Integer> payload) {
        Long userId = userDetails.getAccountDto().getId();
        int quantity = payload.get("quantity");
        CartItemDto updatedItem = cartService.updateCartItemQuantity(userId, itemOptionId, quantity);
        CartPriceInfoDto cartSummary = cartService.getCartSummary(userId);

        return ResponseEntity.ok().body(Map.of(
                "success", true,
                "totalPrice", updatedItem.getTotalPrice(),
                "cartSummary", cartSummary
        ));
    }

    @PostMapping("/remove/{itemId}")
    public String removeFromCart(@AuthenticationPrincipal PrincipalDetails userDetails,
                                 @PathVariable Long itemId) {
        Long userId = userDetails.getAccountDto().getId();
        cartService.removeFromCart(userId, itemId);
        return "redirect:/product/cart";
    }

    @GetMapping
    public String getCartItems(@AuthenticationPrincipal PrincipalDetails userDetails, Model model) {
        Long userId = userDetails.getAccountDto().getId();
        List<CartItemDto> cartItems = cartService.getCartItem(userId);
        CartPriceInfoDto cartSummary = cartService.getCartSummary(userId);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartSummary", cartSummary);

        return "groupbuying/cart";
    }

    @PostMapping("/summary")
    public ResponseEntity<CartPriceInfoDto> getCartSummary(@AuthenticationPrincipal PrincipalDetails userDetails) {
        Long userId = userDetails.getAccountDto().getId();
        CartPriceInfoDto summary = cartService.getCartSummary(userId);
        return ResponseEntity.ok(summary);
    }
}