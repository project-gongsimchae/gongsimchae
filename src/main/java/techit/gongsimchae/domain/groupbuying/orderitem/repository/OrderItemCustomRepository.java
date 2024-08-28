package techit.gongsimchae.domain.groupbuying.orderitem.repository;

import techit.gongsimchae.domain.groupbuying.orderitem.dto.CompletionAmountOrderItemCntDto;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;

import java.util.List;

public interface OrderItemCustomRepository {

    CompletionAmountOrderItemCntDto countCompletedOrderItemsByItemId(Long itemId);

    List<OrderItem> findOrderItemsByItemId(Long itemId);
}
