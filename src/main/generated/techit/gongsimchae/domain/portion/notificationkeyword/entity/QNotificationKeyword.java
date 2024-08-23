package techit.gongsimchae.domain.portion.notificationkeyword.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNotificationKeyword is a Querydsl query type for NotificationKeyword
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotificationKeyword extends EntityPathBase<NotificationKeyword> {

    private static final long serialVersionUID = -1299296712L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNotificationKeyword notificationKeyword = new QNotificationKeyword("notificationKeyword");

    public final techit.gongsimchae.domain.QBaseEntity _super = new techit.gongsimchae.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath keyword = createString("keyword");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final techit.gongsimchae.domain.common.user.entity.QUser user;

    public QNotificationKeyword(String variable) {
        this(NotificationKeyword.class, forVariable(variable), INITS);
    }

    public QNotificationKeyword(Path<? extends NotificationKeyword> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNotificationKeyword(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNotificationKeyword(PathMetadata metadata, PathInits inits) {
        this(NotificationKeyword.class, metadata, inits);
    }

    public QNotificationKeyword(Class<? extends NotificationKeyword> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new techit.gongsimchae.domain.common.user.entity.QUser(forProperty("user")) : null;
    }

}

