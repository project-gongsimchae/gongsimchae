package techit.gongsimchae.domain.groupbuying.couponuser.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCouponUser is a Querydsl query type for CouponUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouponUser extends EntityPathBase<CouponUser> {

    private static final long serialVersionUID = -1667212480L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCouponUser couponUser = new QCouponUser("couponUser");

    public final techit.gongsimchae.domain.groupbuying.coupon.entity.QCoupon coupon;

    public final NumberPath<Integer> couponStatus = createNumber("couponStatus", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final techit.gongsimchae.domain.common.user.entity.QUser user;

    public QCouponUser(String variable) {
        this(CouponUser.class, forVariable(variable), INITS);
    }

    public QCouponUser(Path<? extends CouponUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCouponUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCouponUser(PathMetadata metadata, PathInits inits) {
        this(CouponUser.class, metadata, inits);
    }

    public QCouponUser(Class<? extends CouponUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coupon = inits.isInitialized("coupon") ? new techit.gongsimchae.domain.groupbuying.coupon.entity.QCoupon(forProperty("coupon"), inits.get("coupon")) : null;
        this.user = inits.isInitialized("user") ? new techit.gongsimchae.domain.common.user.entity.QUser(forProperty("user")) : null;
    }

}

