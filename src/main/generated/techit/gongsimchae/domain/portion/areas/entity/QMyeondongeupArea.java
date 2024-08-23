package techit.gongsimchae.domain.portion.areas.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMyeondongeupArea is a Querydsl query type for MyeondongeupArea
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyeondongeupArea extends EntityPathBase<MyeondongeupArea> {

    private static final long serialVersionUID = -937883585L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMyeondongeupArea myeondongeupArea = new QMyeondongeupArea("myeondongeupArea");

    public final techit.gongsimchae.domain.QBaseEntity _super = new techit.gongsimchae.domain.QBaseEntity(this);

    public final StringPath admCode = createString("admCode");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final QSigunguArea sigunguArea;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QMyeondongeupArea(String variable) {
        this(MyeondongeupArea.class, forVariable(variable), INITS);
    }

    public QMyeondongeupArea(Path<? extends MyeondongeupArea> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMyeondongeupArea(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMyeondongeupArea(PathMetadata metadata, PathInits inits) {
        this(MyeondongeupArea.class, metadata, inits);
    }

    public QMyeondongeupArea(Class<? extends MyeondongeupArea> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sigunguArea = inits.isInitialized("sigunguArea") ? new QSigunguArea(forProperty("sigunguArea"), inits.get("sigunguArea")) : null;
    }

}

