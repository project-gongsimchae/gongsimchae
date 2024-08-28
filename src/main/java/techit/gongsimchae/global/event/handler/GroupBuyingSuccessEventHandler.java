package techit.gongsimchae.global.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import techit.gongsimchae.domain.groupbuying.delivery.service.DeliveryService;
import techit.gongsimchae.domain.groupbuying.item.entity.ItemStatus;
import techit.gongsimchae.domain.groupbuying.item.service.ItemService;
import techit.gongsimchae.domain.groupbuying.orderitem.service.OrderItemService;
import techit.gongsimchae.global.event.TargetCountAchievedEvent;

@Component
@RequiredArgsConstructor
public class GroupBuyingSuccessEventHandler {

    private final ItemService itemService;
    private final OrderItemService orderItemService;
    private final DeliveryService deliveryService;

    @EventListener
    public void changeItemStatus(TargetCountAchievedEvent event) {
        itemService.changeItemStatus(event.getItemId(), ItemStatus.공동구매_마감);
    }

    @EventListener
    public void changeOrderItemStatus(TargetCountAchievedEvent event) {

    }

    @EventListener
    public void sendNotification(TargetCountAchievedEvent event) {

    }

    @EventListener
    public void createDelivery(TargetCountAchievedEvent event) {

    }
}
