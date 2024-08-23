package techit.gongsimchae.domain.groupbuying.eventcategory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEventCategory is a Querydsl query type for EventCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEventCategory extends EntityPathBase<EventCategory> {

    private static final long serialVersionUID = 851454852L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEventCategory eventCategory = new QEventCategory("eventCategory");

    public final techit.gongsimchae.domain.groupbuying.category.entity.QCategory category;

    public final techit.gongsimchae.domain.groupbuying.event.entity.QEvent event;

    public final NumberPath<Integer> eventCategoryStatus = createNumber("eventCategoryStatus", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QEventCategory(String variable) {
        this(EventCategory.class, forVariable(variable), INITS);
    }

    public QEventCategory(Path<? extends EventCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEventCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEventCategory(PathMetadata metadata, PathInits inits) {
        this(EventCategory.class, metadata, inits);
    }

    public QEventCategory(Class<? extends EventCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new techit.gongsimchae.domain.groupbuying.category.entity.QCategory(forProperty("category")) : null;
        this.event = inits.isInitialized("event") ? new techit.gongsimchae.domain.groupbuying.event.entity.QEvent(forProperty("event")) : null;
    }

}

