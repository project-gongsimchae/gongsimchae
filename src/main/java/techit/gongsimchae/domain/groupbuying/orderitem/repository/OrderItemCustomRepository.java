package techit.gongsimchae.domain.groupbuying.orderitem.repository;

import techit.gongsimchae.domain.groupbuying.orderitem.dto.CompletionAmountOrderItemCntDto;

public interface OrderItemCustomRepository {

    CompletionAmountOrderItemCntDto countCompletedOrderItemsByItemId(Long itemId);
}
