package techit.gongsimchae.global.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.service.UserService;
import techit.gongsimchae.domain.groupbuying.delivery.service.DeliveryService;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.item.entity.ItemStatus;
import techit.gongsimchae.domain.groupbuying.item.service.ItemService;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItemStatus;
import techit.gongsimchae.domain.groupbuying.orderitem.service.OrderItemService;
import techit.gongsimchae.domain.portion.notifications.event.GroupBuyingNotiEvent;
import techit.gongsimchae.global.event.TargetCountAchievedEvent;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GroupBuyingSuccessEventHandler {

    private final ItemService itemService;
    private final OrderItemService orderItemService;
    private final DeliveryService deliveryService;
    private final ApplicationEventPublisher publisher;
    private final UserService userService;

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
    public void sendNotification(TargetCountAchievedEvent event) {
        List<User> users = userService.findUsersByItemId(event.getItemId());
        Item item = itemService.getItemById(event.getItemId());

        for (User user : users) {
            publisher.publishEvent(new GroupBuyingNotiEvent(user, item.getName() + "에 대한 공동구매가 성공했습니다! 배송 준비 중에 있으니 잠시만 기다려주세요."));
        }
    }

    @Async
    @Order(4)
    @EventListener
    public void createDelivery(TargetCountAchievedEvent event) {
        List<OrderItem> orderItems = orderItemService.getOrderItemsByItemId(event.getItemId());

        for (OrderItem orderItem : orderItems) {
            deliveryService.registerDelivery(orderItem);
        }
    }
}
