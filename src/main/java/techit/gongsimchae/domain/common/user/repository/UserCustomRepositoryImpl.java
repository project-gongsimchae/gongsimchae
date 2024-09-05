package techit.gongsimchae.domain.common.user.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import techit.gongsimchae.domain.common.address.entity.DefaultAddressStatus;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItemStatus;
import techit.gongsimchae.domain.portion.notifications.dto.NotificationKeywordUserDto;

import java.util.ArrayList;
import java.util.List;

import static techit.gongsimchae.domain.common.address.entity.QAddress.address1;
import static techit.gongsimchae.domain.common.user.entity.QUser.user;
import static techit.gongsimchae.domain.groupbuying.item.entity.QItem.item;
import static techit.gongsimchae.domain.groupbuying.itemoption.entity.QItemOption.itemOption;
import static techit.gongsimchae.domain.groupbuying.orderitem.entity.QOrderItem.orderItem;
import static techit.gongsimchae.domain.groupbuying.orders.entity.QOrders.orders;
import static techit.gongsimchae.domain.portion.notificationkeyword.entity.QNotificationKeyword.notificationKeyword;
import static techit.gongsimchae.domain.portion.report.entity.QReport.report;
import static techit.gongsimchae.domain.portion.subdivision.entity.QSubdivision.subdivision;

@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository{
    private final JPAQueryFactory queryFactory;
    @Override
    public List<NotificationKeywordUserDto> findUsersByKeyword(String targetAddress, String keyword) {
        List<Long> subQuery = queryFactory.select(user.id)
                .from(address1)
                .join(address1.user, user)
                .where(address1.defaultAddressStatus.eq(DefaultAddressStatus.PRIMARY).and(address1.sigungu.contains(targetAddress)))
                .fetch();

        List<Tuple> result = queryFactory.select(user, notificationKeyword.keyword)
                .from(notificationKeyword)
                .join(notificationKeyword.user, user)
                .where(notificationKeyword.keyword.eq(keyword).and(user.id.in(subQuery)))
                .fetch();
        List<NotificationKeywordUserDto> list = new ArrayList<>();
        for (Tuple tuple : result) {
            list.add(new NotificationKeywordUserDto(tuple.get(user), tuple.get(notificationKeyword.keyword)));
        }
        return list;
    }

    @Override
    public Page<UserRespDtoWeb> findUsersWithReportCounts(Pageable pageable) {
        List<UserRespDtoWeb> results = queryFactory.select(Projections.fields(UserRespDtoWeb.class, user.id, user.nickname, user.loginId, user.name,
                        user.mannerPoint, user.role, user.userStatus, user.createDate, user.updateDate, user.email, user.joinType))
                .from(user)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(user.createDate.desc())
                .fetch();

        for (UserRespDtoWeb result : results) {
            Long reportCount = queryFactory.select(report.count())
                    .from(report)
                    .join(report.subdivision, subdivision)
                    .join(subdivision.user, user)
                    .where(user.id.eq(result.getId()))
                    .fetchOne();
            result.setReportCount(reportCount);
        }

        Long size = queryFactory.select(user.count())
                .from(user)
                .fetchOne();

        return new PageImpl<>(results, pageable, size);
    }

    @Override
    public List<User> findUsersByItemIdWithOrderItem(Long itemId) {
        List<User> result = queryFactory.selectDistinct(Projections.fields(User.class,
                        user.id, user.name, user.password, user.loginId, user.role, user.email, user.nickname, user.phoneNumber,
                        user.mannerPoint, user.userStatus, user.UID, user.joinType)).from(orderItem)
                .join(orderItem.orders.user, user)
                .join(orderItem.itemOption, itemOption)
                .join(itemOption.item, item)
                .where(item.id.eq(itemId).and(orderItem.orderItemStatus.eq(OrderItemStatus.공동구매성공)))
                .fetch();

        return result;
    }
}
