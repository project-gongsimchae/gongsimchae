package techit.gongsimchae.domain.groupbuying.couponcategory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCouponCategory is a Querydsl query type for CouponCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouponCategory extends EntityPathBase<CouponCategory> {

    private static final long serialVersionUID = -755816602L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCouponCategory couponCategory = new QCouponCategory("couponCategory");

    public final techit.gongsimchae.domain.groupbuying.category.entity.QCategory category;

    public final techit.gongsimchae.domain.groupbuying.coupon.entity.QCoupon coupon;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QCouponCategory(String variable) {
        this(CouponCategory.class, forVariable(variable), INITS);
    }

    public QCouponCategory(Path<? extends CouponCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCouponCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCouponCategory(PathMetadata metadata, PathInits inits) {
        this(CouponCategory.class, metadata, inits);
    }

    public QCouponCategory(Class<? extends CouponCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new techit.gongsimchae.domain.groupbuying.category.entity.QCategory(forProperty("category")) : null;
        this.coupon = inits.isInitialized("coupon") ? new techit.gongsimchae.domain.groupbuying.coupon.entity.QCoupon(forProperty("coupon"), inits.get("coupon")) : null;
    }

}

