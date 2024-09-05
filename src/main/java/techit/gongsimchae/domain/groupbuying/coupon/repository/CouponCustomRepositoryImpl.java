package techit.gongsimchae.domain.groupbuying.coupon.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import techit.gongsimchae.domain.groupbuying.coupon.dto.CouponRespDtoWeb;

import java.util.List;

import static techit.gongsimchae.domain.common.user.entity.QUser.user;
import static techit.gongsimchae.domain.groupbuying.coupon.entity.QCoupon.coupon;
import static techit.gongsimchae.domain.groupbuying.couponuser.entity.QCouponUser.couponUser;

public class CouponCustomRepositoryImpl implements CouponCustomRepository{
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    public CouponCustomRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CouponRespDtoWeb> findCouponsByUserId(Long userId) {
        return queryFactory.select(Projections.fields(CouponRespDtoWeb.class,
                        coupon.id, coupon.discountRate, coupon.maxDiscount, coupon.expirationDate, coupon.couponName, coupon.couponCode))
                .from(couponUser)
                .leftJoin(couponUser.coupon, coupon)
                .join(couponUser.user, user)
                .where(user.id.eq(userId))
                .where(couponUser.couponStatus.eq(0))
                .fetch();
    }


}
