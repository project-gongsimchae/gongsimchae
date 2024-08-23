package techit.gongsimchae.domain.portion.areas.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSido is a Querydsl query type for Sido
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSido extends EntityPathBase<Sido> {

    private static final long serialVersionUID = -1837674737L;

    public static final QSido sido = new QSido("sido");

    public final techit.gongsimchae.domain.QBaseEntity _super = new techit.gongsimchae.domain.QBaseEntity(this);

    public final StringPath admCode = createString("admCode");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<Sigungu, QSigungu> sigungus = this.<Sigungu, QSigungu>createList("sigungus", Sigungu.class, QSigungu.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QSido(String variable) {
        super(Sido.class, forVariable(variable));
    }

    public QSido(Path<? extends Sido> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSido(PathMetadata metadata) {
        super(Sido.class, metadata);
    }

}

