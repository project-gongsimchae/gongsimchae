package techit.gongsimchae.domain.groupbuying.orders.dto;

import lombok.Builder;
import lombok.Data;
import techit.gongsimchae.domain.groupbuying.cart.entity.Cart;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.itemoption.entity.ItemOption;

@Data
@Builder
public class TempOrderItemDto {
    private Long itemOptionId;
    private String itemName;
    private String optionName;
    private int quantity;
    private int OriginalPrice;
    private int price;
    private int totalPrice;
    private Long categoryId;
    public static TempOrderItemDto fromCart(Cart cart) {
        ItemOption option = cart.getItemOption();
        Item item = option.getItem();
        int price = item.getOriginalPrice() + option.getPrice();
        int discountedPrice = (int) (price * (100 - item.getDiscountRate()) / 100.0);

        return TempOrderItemDto.builder()
                .itemOptionId(option.getId())
                .itemName(item.getName())
                .optionName(option.getContent())
                .quantity(cart.getCount())
                .OriginalPrice(price)
                .price(discountedPrice)
                .totalPrice(discountedPrice * cart.getCount())
                .categoryId(item.getCategory().getId())
                .build();
    }
}
