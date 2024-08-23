package techit.gongsimchae.domain.portion.areas.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSidoArea is a Querydsl query type for SidoArea
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSidoArea extends EntityPathBase<SidoArea> {

    private static final long serialVersionUID = -651529156L;

    public static final QSidoArea sidoArea = new QSidoArea("sidoArea");

    public final techit.gongsimchae.domain.QBaseEntity _super = new techit.gongsimchae.domain.QBaseEntity(this);

    public final StringPath admCode = createString("admCode");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<SigunguArea, QSigunguArea> sigunguAreas = this.<SigunguArea, QSigunguArea>createList("sigunguAreas", SigunguArea.class, QSigunguArea.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QSidoArea(String variable) {
        super(SidoArea.class, forVariable(variable));
    }

    public QSidoArea(Path<? extends SidoArea> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSidoArea(PathMetadata metadata) {
        super(SidoArea.class, metadata);
    }

}

