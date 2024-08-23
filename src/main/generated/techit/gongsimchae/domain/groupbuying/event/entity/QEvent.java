package techit.gongsimchae.domain.groupbuying.event.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEvent is a Querydsl query type for Event
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEvent extends EntityPathBase<Event> {

    private static final long serialVersionUID = -12666972L;

    public static final QEvent event = new QEvent("event");

    public final NumberPath<Integer> discountRate = createNumber("discountRate", Integer.class);

    public final StringPath eventName = createString("eventName");

    public final NumberPath<Integer> eventStatus = createNumber("eventStatus", Integer.class);

    public final EnumPath<EventType> eventType = createEnum("eventType", EventType.class);

    public final DateTimePath<java.time.LocalDateTime> expirationDate = createDateTime("expirationDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> maxDiscount = createNumber("maxDiscount", Integer.class);

    public QEvent(String variable) {
        super(Event.class, forVariable(variable));
    }

    public QEvent(Path<? extends Event> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEvent(PathMetadata metadata) {
        super(Event.class, metadata);
    }

}

