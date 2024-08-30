package techit.gongsimchae.global.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import techit.gongsimchae.domain.groupbuying.item.entity.ItemStatus;
import techit.gongsimchae.domain.groupbuying.item.service.ItemService;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItemStatus;
import techit.gongsimchae.domain.groupbuying.orderitem.service.OrderItemService;
import techit.gongsimchae.global.event.TargetCountAchievedFailEvent;

@Component
@RequiredArgsConstructor
public class GroupBuyingFailEventHandler {

    private final ItemService itemService;
    private final OrderItemService orderItemService;

    @Async
    @Order(1)
    @EventListener
    public void changeItemStatus(TargetCountAchievedFailEvent event) {
        itemService.changeItemStatus(event.getItemId(), ItemStatus.공동구매_마감);
    }

    @Async
    @Order(2)
    @EventListener
    public void changeOrderItemStatus(TargetCountAchievedFailEvent event) {
        orderItemService.changeOrderItemStatusWithItemId(event.getItemId(), OrderItemStatus.공동구매실패);
    }

    @Async
    @Order(3)
    @EventListener
    public void refundMoney(TargetCountAchievedFailEvent event) {
        // TODO 환불 로직 작성
    }
}
