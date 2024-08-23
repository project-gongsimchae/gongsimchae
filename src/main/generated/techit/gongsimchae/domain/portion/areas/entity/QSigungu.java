package techit.gongsimchae.domain.portion.areas.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSigungu is a Querydsl query type for Sigungu
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSigungu extends EntityPathBase<Sigungu> {

    private static final long serialVersionUID = 1783090474L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSigungu sigungu = new QSigungu("sigungu");

    public final techit.gongsimchae.domain.QBaseEntity _super = new techit.gongsimchae.domain.QBaseEntity(this);

    public final StringPath admCode = createString("admCode");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<MyeonDongEup, QMyeonDongEup> myeonDongEups = this.<MyeonDongEup, QMyeonDongEup>createList("myeonDongEups", MyeonDongEup.class, QMyeonDongEup.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final QSido sido;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QSigungu(String variable) {
        this(Sigungu.class, forVariable(variable), INITS);
    }

    public QSigungu(Path<? extends Sigungu> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSigungu(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSigungu(PathMetadata metadata, PathInits inits) {
        this(Sigungu.class, metadata, inits);
    }

    public QSigungu(Class<? extends Sigungu> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sido = inits.isInitialized("sido") ? new QSido(forProperty("sido")) : null;
    }

}

