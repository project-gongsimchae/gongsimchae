package techit.gongsimchae.global.event.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import techit.gongsimchae.domain.groupbuying.delivery.service.DeliveryService;
import techit.gongsimchae.domain.groupbuying.item.entity.ItemStatus;
import techit.gongsimchae.domain.groupbuying.item.service.ItemService;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItemStatus;
import techit.gongsimchae.domain.groupbuying.orderitem.service.OrderItemService;
import techit.gongsimchae.global.event.TargetCountAchievedEvent;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupBuyingSuccessEventHandler {

    private final ItemService itemService;
    private final OrderItemService orderItemService;
    private final DeliveryService deliveryService;

    @Async
    @Order(1)
    @EventListener
    public void changeItemStatus(TargetCountAchievedEvent event) {
        itemService.changeItemStatus(event.getItemId(), ItemStatus.공동구매_마감);
    }

    @Async
    @Order(2)
    @EventListener
    public void changeOrderItemStatus(TargetCountAchievedEvent event) {
        orderItemService.changeOrderItemStatusWithItemId(event.getItemId(), OrderItemStatus.공동구매성공);
    }

    @Async
    @Order(3)
    @EventListener
    public void createDelivery(TargetCountAchievedEvent event) {
        List<OrderItem> orderItems = orderItemService.getOrderItemsByItemId(event.getItemId());

        for (OrderItem orderItem : orderItems) {
            deliveryService.registerDelivery(orderItem);
        }
    }
}
