package techit.gongsimchae.domain.groupbuying.payment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPayment is a Querydsl query type for Payment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPayment extends EntityPathBase<Payment> {

    private static final long serialVersionUID = 556786532L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPayment payment = new QPayment("payment");

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final StringPath customerName = createString("customerName");

    public final StringPath failUrl = createString("failUrl");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath orderName = createString("orderName");

    public final techit.gongsimchae.domain.groupbuying.orders.entity.QOrders orders;

    public final DateTimePath<java.time.LocalDateTime> payDate = createDateTime("payDate", java.time.LocalDateTime.class);

    public final StringPath paymentApproveId = createString("paymentApproveId");

    public final BooleanPath payStatus = createBoolean("payStatus");

    public final StringPath payType = createString("payType");

    public final StringPath successUrl = createString("successUrl");

    public QPayment(String variable) {
        this(Payment.class, forVariable(variable), INITS);
    }

    public QPayment(Path<? extends Payment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPayment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPayment(PathMetadata metadata, PathInits inits) {
        this(Payment.class, metadata, inits);
    }

    public QPayment(Class<? extends Payment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.orders = inits.isInitialized("orders") ? new techit.gongsimchae.domain.groupbuying.orders.entity.QOrders(forProperty("orders"), inits.get("orders")) : null;
    }

}

