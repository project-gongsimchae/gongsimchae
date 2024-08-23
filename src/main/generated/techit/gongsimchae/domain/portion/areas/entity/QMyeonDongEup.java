package techit.gongsimchae.domain.portion.areas.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMyeonDongEup is a Querydsl query type for MyeonDongEup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyeonDongEup extends EntityPathBase<MyeonDongEup> {

    private static final long serialVersionUID = -2118747054L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMyeonDongEup myeonDongEup = new QMyeonDongEup("myeonDongEup");

    public final techit.gongsimchae.domain.QBaseEntity _super = new techit.gongsimchae.domain.QBaseEntity(this);

    public final StringPath admCode = createString("admCode");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final QSigungu sigungu;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QMyeonDongEup(String variable) {
        this(MyeonDongEup.class, forVariable(variable), INITS);
    }

    public QMyeonDongEup(Path<? extends MyeonDongEup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMyeonDongEup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMyeonDongEup(PathMetadata metadata, PathInits inits) {
        this(MyeonDongEup.class, metadata, inits);
    }

    public QMyeonDongEup(Class<? extends MyeonDongEup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sigungu = inits.isInitialized("sigungu") ? new QSigungu(forProperty("sigungu"), inits.get("sigungu")) : null;
    }

}

