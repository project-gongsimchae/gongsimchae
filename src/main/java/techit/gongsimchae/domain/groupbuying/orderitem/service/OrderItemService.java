package techit.gongsimchae.domain.groupbuying.orderitem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.groupbuying.orderitem.dto.CompletionAmountOrderItemCntDto;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItemStatus;
import techit.gongsimchae.domain.groupbuying.orderitem.repository.OrderItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    /**
     * 해당 아이템을 주문해서 결제 완료한 수량이 몇 인지 카운트하는 메서드입니다.
     *
     * @param itemId
     * @return
     */
    public CompletionAmountOrderItemCntDto getCountCompletedOrderItemsWithItemId(Long itemId) {
        return orderItemRepository.countCompletedOrderItemsByItemId(itemId);
    }

    /**
     * 공동구매가 성공되었을 때 이벤트 리스너로 실행되는 메서드입니다.
     * 해당 itemId로 결제까지 완료한 OrderItem들을 찾아 그 상태 값을 파라미터로 넘겨받은 orderItemStatus로 바꿔주는 메서드입니다.
     *
     * @param itemId
     * @param orderItemStatus
     */
    public void changeOrderItemStatusWithItemId(Long itemId, OrderItemStatus orderItemStatus) {
        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByItemId(itemId);

        for (OrderItem orderItem : orderItems) {
            orderItem.updateOrderItemStatus(orderItemStatus);
        }
    }

    /**
     * 해당 itemId로 orderItem들을 찾아 반환하는 메서드입니다.
     *
     * @param itemId
     * @return
     */
    public List<OrderItem> getOrderItemsByItemId(Long itemId) {
        return orderItemRepository.findOrderItemsByItemId(itemId);
    }
}
