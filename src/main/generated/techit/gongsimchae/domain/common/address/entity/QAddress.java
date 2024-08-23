package techit.gongsimchae.domain.common.address.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAddress is a Querydsl query type for Address
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAddress extends EntityPathBase<Address> {

    private static final long serialVersionUID = 948162936L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAddress address1 = new QAddress("address1");

    public final StringPath additionalAddress = createString("additionalAddress");

    public final StringPath address = createString("address");

    public final EnumPath<DefaultAddressStatus> defaultAddressStatus = createEnum("defaultAddressStatus", DefaultAddressStatus.class);

    public final StringPath detailAddress = createString("detailAddress");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath receiver = createString("receiver");

    public final StringPath sigungu = createString("sigungu");

    public final StringPath UID = createString("UID");

    public final techit.gongsimchae.domain.common.user.entity.QUser user;

    public final StringPath zipcode = createString("zipcode");

    public QAddress(String variable) {
        this(Address.class, forVariable(variable), INITS);
    }

    public QAddress(Path<? extends Address> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAddress(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAddress(PathMetadata metadata, PathInits inits) {
        this(Address.class, metadata, inits);
    }

    public QAddress(Class<? extends Address> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new techit.gongsimchae.domain.common.user.entity.QUser(forProperty("user")) : null;
    }

}

