package techit.gongsimchae.domain.groupbuying.orderitem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.groupbuying.orderitem.dto.CompletionAmountOrderItemCntDto;
import techit.gongsimchae.domain.groupbuying.orderitem.repository.OrderItemRepository;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public CompletionAmountOrderItemCntDto getCountCompletedOrderItemsWithItemId(Long itemId) {
        return orderItemRepository.countCompletedOrderItemsByItemId(itemId);
    }
}
