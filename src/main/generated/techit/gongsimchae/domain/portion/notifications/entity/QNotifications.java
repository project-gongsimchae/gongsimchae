package techit.gongsimchae.domain.portion.notifications.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNotifications is a Querydsl query type for Notifications
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotifications extends EntityPathBase<Notifications> {

    private static final long serialVersionUID = -1285832744L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNotifications notifications = new QNotifications("notifications");

    public final techit.gongsimchae.domain.QBaseEntity _super = new techit.gongsimchae.domain.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> isRead = createNumber("isRead", Integer.class);

    public final EnumPath<NotificationType> notificationType = createEnum("notificationType", NotificationType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final StringPath url = createString("url");

    public final techit.gongsimchae.domain.common.user.entity.QUser user;

    public QNotifications(String variable) {
        this(Notifications.class, forVariable(variable), INITS);
    }

    public QNotifications(Path<? extends Notifications> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNotifications(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNotifications(PathMetadata metadata, PathInits inits) {
        this(Notifications.class, metadata, inits);
    }

    public QNotifications(Class<? extends Notifications> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new techit.gongsimchae.domain.common.user.entity.QUser(forProperty("user")) : null;
    }

}

