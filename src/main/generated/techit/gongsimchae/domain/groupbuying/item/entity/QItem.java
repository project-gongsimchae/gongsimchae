package techit.gongsimchae.domain.groupbuying.item.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = -1166814108L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItem item = new QItem("item");

    public final techit.gongsimchae.domain.QBaseEntity _super = new techit.gongsimchae.domain.QBaseEntity(this);

    public final techit.gongsimchae.domain.groupbuying.category.entity.QCategory category;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> cumulativeSalesVolume = createNumber("cumulativeSalesVolume", Long.class);

    public final BooleanPath deleteStatus = createBoolean("deleteStatus");

    public final NumberPath<Integer> discountRate = createNumber("discountRate", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> groupBuyingLimitTime = createDateTime("groupBuyingLimitTime", java.time.LocalDateTime.class);

    public final NumberPath<Integer> groupBuyingQuantity = createNumber("groupBuyingQuantity", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<techit.gongsimchae.domain.common.imagefile.entity.ImageFile, techit.gongsimchae.domain.common.imagefile.entity.QImageFile> imageFiles = this.<techit.gongsimchae.domain.common.imagefile.entity.ImageFile, techit.gongsimchae.domain.common.imagefile.entity.QImageFile>createList("imageFiles", techit.gongsimchae.domain.common.imagefile.entity.ImageFile.class, techit.gongsimchae.domain.common.imagefile.entity.QImageFile.class, PathInits.DIRECT2);

    public final StringPath intro = createString("intro");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> originalPrice = createNumber("originalPrice", Integer.class);

    public final NumberPath<Integer> pointAccumulationRate = createNumber("pointAccumulationRate", Integer.class);

    public final NumberPath<Long> reviewCount = createNumber("reviewCount", Long.class);

    public final StringPath UID = createString("UID");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QItem(String variable) {
        this(Item.class, forVariable(variable), INITS);
    }

    public QItem(Path<? extends Item> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItem(PathMetadata metadata, PathInits inits) {
        this(Item.class, metadata, inits);
    }

    public QItem(Class<? extends Item> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new techit.gongsimchae.domain.groupbuying.category.entity.QCategory(forProperty("category")) : null;
    }

}

