package techit.gongsimchae.domain.groupbuying.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.groupbuying.cart.dto.CartAddRequest;
import techit.gongsimchae.domain.groupbuying.cart.dto.CartItemDto;
import techit.gongsimchae.domain.groupbuying.cart.dto.CartPriceInfoDto;
import techit.gongsimchae.domain.groupbuying.cart.entity.Cart;
import techit.gongsimchae.domain.groupbuying.cart.repository.CartRepository;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.itemoption.entity.ItemOption;
import techit.gongsimchae.domain.groupbuying.itemoption.repository.ItemOptionRepository;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;
import techit.gongsimchae.domain.groupbuying.orders.dto.TempOrderItemDto;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ItemOptionRepository itemOptionRepository;
    @Transactional
    public void addToCart(Long userId, List<CartAddRequest.CartItemRequest> cartItems) {

        // 유저 아이디로 유저를 찾는다
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomWebException(ErrorMessage.USER_NOT_FOUND));

        for (CartAddRequest.CartItemRequest item : cartItems){
            ItemOption itemOption = itemOptionRepository.findById(item.getItemOptionId())
                    .orElseThrow(() -> new CustomWebException(ErrorMessage.ITEM_OPTION_NOT_FOUND));

            Cart duplicateCart = cartRepository.findByUserIdAndItemOptionId(userId,itemOption.getId())
                    .orElse(null);

            if (duplicateCart != null) {
                duplicateCart.addCount(item.getQuantity());
            } else {
                Cart cart = new Cart(item.getQuantity(), itemOption, user);
                cartRepository.save(cart);
            }
        }
    }

    @Transactional
    public void removeFromCart(Long userId, Long itemOptionId) {
        cartRepository.deleteCartByUserIdAndItemOptionId(userId, itemOptionId);
    }

    @Transactional
    public void removeMultipleItems(Long userId, List<Long> itemOptionId) {
        cartRepository.deleteAllByUserIdAndItemOptionIdIn(userId, itemOptionId);
    }

    @Transactional
    public CartItemDto updateCartItemQuantity(Long userId, Long itemOptionId, int quantity) {
        Cart cart = cartRepository.findByUserIdAndItemOptionId(userId, itemOptionId)
                .orElseThrow(() -> new CustomWebException("장바구니에 해당 상품이 없습니다. itemOptionId: " + itemOptionId + ", UserId: " + userId + "quantity: " + quantity));

        cart.updateCount(quantity);
        Cart updateCart = cartRepository.save(cart);
        return convertToCartItemDto(updateCart);
    }

    @Transactional(readOnly = true)
    public List<CartItemDto> getCartItem(Long userId){
        List<Cart> cart = cartRepository.findByUserId(userId);
        return cart.stream()
                .map(this::convertToCartItemDto)
                .collect(Collectors.toList());
    }

    private CartItemDto convertToCartItemDto(Cart cart) {
        ItemOption itemOption = cart.getItemOption();
        Item item = itemOption.getItem();

        int originalPrice = item.getOriginalPrice();
        int optionPrice = itemOption.getPrice();
        int totalPrice = (originalPrice + optionPrice) * cart.getCount();
        int discountAmount = (int) (totalPrice * (item.getDiscountRate() / 100.0));
        int discountedPrice = totalPrice - discountAmount;

        return CartItemDto.builder()
                .cartId(cart.getId())
                .itemId(item.getId())
                .itemName(item.getName())
                .itemOptionId(itemOption.getId())
                .content(itemOption.getContent())
                .originalPrice(originalPrice + optionPrice)
                .discountRate(item.getDiscountRate())
                .groupBuyingQuantity(item.getGroupBuyingQuantity())
                .discountPrice(discountedPrice)
                .options(itemOption.getOptions())
                .quantity(cart.getCount())
                .totalPrice(totalPrice)
                .groupBuyingLimitTime(item.getGroupBuyingLimitTime())
                .itemStatus(item.getItemStatus())
                .build();
    }

    @Transactional(readOnly = true)
    public CartPriceInfoDto getCartSummary(Long userId, List<Long> selectedItemOptionIds) {

        List<Cart> cartItems = cartRepository.findByUserIdAndItemOptionIdIn(userId,selectedItemOptionIds);
        int totalOriginalPrice = 0;
        int totalDiscountAmount = 0;

        for (Cart cart : cartItems) {
            Item item = cart.getItemOption().getItem();
            ItemOption itemOption = cart.getItemOption();

            int originalPrice = (item.getOriginalPrice() + itemOption.getPrice()) * cart.getCount();
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

    public List<TempOrderItemDto> getCartItemFromOrderItem(Long userId, List<Long> selectedItemOptionId){
        List<Cart> cart = cartRepository.findByUserIdAndItemOptionIdIn(userId,selectedItemOptionId);
        return cart.stream()
                .map(TempOrderItemDto::fromCart)
                .collect(Collectors.toList());
    }

}
