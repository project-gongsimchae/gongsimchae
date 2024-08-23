package techit.gongsimchae.domain.common.user.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import techit.gongsimchae.domain.common.address.entity.DefaultAddressStatus;
import techit.gongsimchae.domain.portion.notifications.dto.NotificationKeywordUserDto;

import java.util.ArrayList;
import java.util.List;

import static techit.gongsimchae.domain.common.address.entity.QAddress.address1;
import static techit.gongsimchae.domain.common.user.entity.QUser.user;
import static techit.gongsimchae.domain.portion.notificationkeyword.entity.QNotificationKeyword.notificationKeyword;

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
}
