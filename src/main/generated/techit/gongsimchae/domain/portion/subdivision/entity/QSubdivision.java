package techit.gongsimchae.domain.portion.subdivision.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSubdivision is a Querydsl query type for Subdivision
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubdivision extends EntityPathBase<Subdivision> {

    private static final long serialVersionUID = 1133518072L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSubdivision subdivision = new QSubdivision("subdivision");

    public final techit.gongsimchae.domain.QBaseEntity _super = new techit.gongsimchae.domain.QBaseEntity(this);

    public final StringPath address = createString("address");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final BooleanPath deleteStatus = createBoolean("deleteStatus");

    public final StringPath dong = createString("dong");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<techit.gongsimchae.domain.common.imagefile.entity.ImageFile, techit.gongsimchae.domain.common.imagefile.entity.QImageFile> imageFileList = this.<techit.gongsimchae.domain.common.imagefile.entity.ImageFile, techit.gongsimchae.domain.common.imagefile.entity.QImageFile>createList("imageFileList", techit.gongsimchae.domain.common.imagefile.entity.ImageFile.class, techit.gongsimchae.domain.common.imagefile.entity.QImageFile.class, PathInits.DIRECT2);

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final NumberPath<Integer> numOfParticipants = createNumber("numOfParticipants", Integer.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final StringPath sigungu = createString("sigungu");

    public final EnumPath<SubdivisionType> subdivisionType = createEnum("subdivisionType", SubdivisionType.class);

    public final StringPath title = createString("title");

    public final StringPath UID = createString("UID");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final techit.gongsimchae.domain.common.user.entity.QUser user;

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public QSubdivision(String variable) {
        this(Subdivision.class, forVariable(variable), INITS);
    }

    public QSubdivision(Path<? extends Subdivision> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSubdivision(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSubdivision(PathMetadata metadata, PathInits inits) {
        this(Subdivision.class, metadata, inits);
    }

    public QSubdivision(Class<? extends Subdivision> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new techit.gongsimchae.domain.common.user.entity.QUser(forProperty("user")) : null;
    }

}

