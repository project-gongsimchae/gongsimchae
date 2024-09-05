package techit.gongsimchae.domain.common.participate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QParticipate is a Querydsl query type for Participate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QParticipate extends EntityPathBase<Participate> {

    private static final long serialVersionUID = -1321271112L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QParticipate participate = new QParticipate("participate");

    public final techit.gongsimchae.domain.QBaseEntity _super = new techit.gongsimchae.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final techit.gongsimchae.domain.groupbuying.item.entity.QItem item;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final techit.gongsimchae.domain.common.user.entity.QUser user;

    public QParticipate(String variable) {
        this(Participate.class, forVariable(variable), INITS);
    }

    public QParticipate(Path<? extends Participate> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QParticipate(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QParticipate(PathMetadata metadata, PathInits inits) {
        this(Participate.class, metadata, inits);
    }

    public QParticipate(Class<? extends Participate> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new techit.gongsimchae.domain.groupbuying.item.entity.QItem(forProperty("item"), inits.get("item")) : null;
        this.user = inits.isInitialized("user") ? new techit.gongsimchae.domain.common.user.entity.QUser(forProperty("user")) : null;
    }

}

