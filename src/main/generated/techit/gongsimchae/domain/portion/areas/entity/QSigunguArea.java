package techit.gongsimchae.domain.portion.areas.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSigunguArea is a Querydsl query type for SigunguArea
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSigunguArea extends EntityPathBase<SigunguArea> {

    private static final long serialVersionUID = 973630679L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSigunguArea sigunguArea = new QSigunguArea("sigunguArea");

    public final techit.gongsimchae.domain.QBaseEntity _super = new techit.gongsimchae.domain.QBaseEntity(this);

    public final StringPath admCode = createString("admCode");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<MyeondongeupArea, QMyeondongeupArea> myeondongeupAreas = this.<MyeondongeupArea, QMyeondongeupArea>createList("myeondongeupAreas", MyeondongeupArea.class, QMyeondongeupArea.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final QSidoArea sidoArea;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QSigunguArea(String variable) {
        this(SigunguArea.class, forVariable(variable), INITS);
    }

    public QSigunguArea(Path<? extends SigunguArea> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSigunguArea(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSigunguArea(PathMetadata metadata, PathInits inits) {
        this(SigunguArea.class, metadata, inits);
    }

    public QSigunguArea(Class<? extends SigunguArea> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sidoArea = inits.isInitialized("sidoArea") ? new QSidoArea(forProperty("sidoArea")) : null;
    }

}

