package techit.gongsimchae.domain.groupbuying.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.item.entity.ItemStatus;
import techit.gongsimchae.domain.groupbuying.item.repository.ItemRepository;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;
import techit.gongsimchae.domain.groupbuying.orderitem.service.OrderItemService;
import techit.gongsimchae.domain.portion.notifications.event.GroupBuyingNotiEvent;
import techit.gongsimchae.global.event.TargetCountAchievedFailEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemScheduler {

    private final ItemRepository itemRepository;
    private final OrderItemService orderItemService;
    private final ApplicationEventPublisher publisher;

    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
    @Transactional
    public void checkAndChangeItemStatus() {
        LocalDateTime now = LocalDateTime.now();

        List<Item> items = itemRepository.findAllByItemStatusAndGroupBuyingLimitTimeBefore(ItemStatus.공동구매_진행중, now);

        for (Item item : items) {
            int completionAmountCnt = orderItemService.getCountCompletedOrderItemsWithItemId(item.getId()).getCompletionAmountCnt();

            // 주문 수량이 목표 수량보다 작을 때 공동 구매 실패 로직 처리
            if (item.getGroupBuyingQuantity() > completionAmountCnt) {
                publisher.publishEvent(new TargetCountAchievedFailEvent(item.getId()));

                List<OrderItem> orderItems = orderItemService.getOrderItemsByItemId(item.getId());
                List<User> users = orderItems.stream().map(orderItem -> orderItem.getOrders().getUser()).distinct().toList();

                for (User user : users) {
                    publisher.publishEvent(new GroupBuyingNotiEvent(user, item.getName() + " 상품에 대한 공동구매가 실패하였습니다. 곧 환불 조치를 취할 예정이니 잠시만 기다려주세요."));
                }

            }
        }

        itemRepository.saveAll(items);
    }

}
