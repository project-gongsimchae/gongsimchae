package techit.gongsimchae.domain.groupbuying.reviews.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviews is a Querydsl query type for Reviews
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviews extends EntityPathBase<Reviews> {

    private static final long serialVersionUID = -672475452L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviews reviews = new QReviews("reviews");

    public final techit.gongsimchae.domain.QBaseEntity _super = new techit.gongsimchae.domain.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final techit.gongsimchae.domain.groupbuying.item.entity.QItem item;

    public final EnumPath<SecretStatus> secretStatus = createEnum("secretStatus", SecretStatus.class);

    public final NumberPath<Integer> starPoint = createNumber("starPoint", Integer.class);

    public final StringPath title = createString("title");

    public final StringPath UID = createString("UID");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final techit.gongsimchae.domain.common.user.entity.QUser user;

    public QReviews(String variable) {
        this(Reviews.class, forVariable(variable), INITS);
    }

    public QReviews(Path<? extends Reviews> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviews(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviews(PathMetadata metadata, PathInits inits) {
        this(Reviews.class, metadata, inits);
    }

    public QReviews(Class<? extends Reviews> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new techit.gongsimchae.domain.groupbuying.item.entity.QItem(forProperty("item"), inits.get("item")) : null;
        this.user = inits.isInitialized("user") ? new techit.gongsimchae.domain.common.user.entity.QUser(forProperty("user")) : null;
    }

}

