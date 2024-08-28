package techit.gongsimchae.domain.groupbuying.orderitem.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import techit.gongsimchae.domain.groupbuying.orderitem.dto.CompletionAmountOrderItemCntDto;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItemStatus;

import static techit.gongsimchae.domain.groupbuying.item.entity.QItem.item;
import static techit.gongsimchae.domain.groupbuying.itemoption.entity.QItemOption.itemOption;
import static techit.gongsimchae.domain.groupbuying.orderitem.entity.QOrderItem.orderItem;

@RequiredArgsConstructor
public class OrderItemCustomRepositoryImpl implements OrderItemCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public CompletionAmountOrderItemCntDto countCompletedOrderItemsByItemId(Long itemId) {
        CompletionAmountOrderItemCntDto result = queryFactory.select(Projections.constructor(CompletionAmountOrderItemCntDto.class,
                orderItem.count.sum())).from(orderItem)
                .join(itemOption.item, item)
                .where(item.id.eq(itemId).and(orderItem.orderItemStatus.eq(OrderItemStatus.결제완료)))
                .fetchOne();

        return result;
    }
}