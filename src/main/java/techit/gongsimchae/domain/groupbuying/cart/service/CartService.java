package techit.gongsimchae.domain.groupbuying.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.groupbuying.cart.dto.CartItemDto;
import techit.gongsimchae.domain.groupbuying.cart.dto.CartPriceInfoDto;
import techit.gongsimchae.domain.groupbuying.cart.entity.Cart;
import techit.gongsimchae.domain.groupbuying.cart.repository.CartRepository;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.item.repository.ItemRepository;
import techit.gongsimchae.global.exception.CustomWebException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void addToCart(Long userId, Long itemId, int count) {

        // 유저 아이디로 유저를 찾는다
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomWebException("유효하지 않은 회원입니다."));

        // 아이템 아이디로 아이디를 찾는다
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new CustomWebException("유효하지 않은 상품입니다."));

        // 카트에 넣은 아이템인지 확인을한다
        Cart duplicateCart = cartRepository.findByUserIdAndItemId(userId, itemId)
                .orElse(null);

        if (duplicateCart != null) {
            duplicateCart.addCount(count);
        } else {
            Cart cart = new Cart(count, item, user);
            cartRepository.save(cart);
        }
    }

    @Transactional
    public void removeFromCart(Long userId, Long itemId) {
        cartRepository.deleteCartByUserIdAndItemId(userId, itemId);
    }

    @Transactional
    public void removeMultipleItems(Long userId, List<Long> itemId) {
        cartRepository.deleteAllByUserIdAndItemIdIn(userId, itemId);
    }

    @Transactional
    public CartItemDto updateCartItemQuantity(Long userId, Long itemId, int quantity) {
        Cart cart = cartRepository.findByUserIdAndItemId(userId, itemId)
                .orElseThrow(() -> new CustomWebException("장바구니에 해당 상품이 없습니다."));

        cart.updateCount(quantity);
        cartRepository.save(cart);
        return convertToCartItemDto(cart);
    }

    @Transactional(readOnly = true)
    public List<CartItemDto> getCartItem(Long userId){
        List<Cart> cart = cartRepository.findByUserId(userId);
        return cart.stream()
                .map(this::convertToCartItemDto)
                .collect(Collectors.toList());
    }

    private CartItemDto convertToCartItemDto(Cart cart) {
        Item item = cart.getItem();
        int originalPrice = item.getOriginalPrice() * cart.getCount();
        int discountPrice = (int) (originalPrice * (item.getDiscountRate() / 100.0));
        int totalPrice = originalPrice - discountPrice;

        return CartItemDto.builder()
                .cartId(cart.getId())
                .itemId(item.getId())
                .itemName(item.getName())
                .originalPrice(originalPrice)
                .discountRate(item.getDiscountRate())
                .groupBuyingQuantity(item.getGroupBuyingQuantity())
                .discountPrice(discountPrice)
                .quantity(cart.getCount())
                .totalPrice(totalPrice)
                .groupBuyingLimitTime(item.getGroupBuyingLimitTime())
                .build();
    }

    @Transactional(readOnly = true)
    public CartPriceInfoDto getCartSummary(Long userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        int totalOriginalPrice = 0;
        int totalDiscountAmount = 0;

        for (Cart cart : cartItems) {
            Item item = cart.getItem();
            int originalPrice = item.getOriginalPrice() * cart.getCount();
            int discountAmount = (int) (originalPrice * (item.getDiscountRate() / 100.0));

            totalOriginalPrice += originalPrice;
            totalDiscountAmount += discountAmount;
        }

        int totalFinalPrice = totalOriginalPrice - totalDiscountAmount;

        return CartPriceInfoDto.builder()
                .totalPrice(totalOriginalPrice)
                .totalDiscount(totalDiscountAmount)
                .totalPaymentPrice(totalFinalPrice)
                .build();
    }


}
