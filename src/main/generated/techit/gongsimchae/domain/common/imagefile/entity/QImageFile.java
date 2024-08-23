package techit.gongsimchae.domain.common.imagefile.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QImageFile is a Querydsl query type for ImageFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QImageFile extends EntityPathBase<ImageFile> {

    private static final long serialVersionUID = 2133806520L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QImageFile imageFile = new QImageFile("imageFile");

    public final techit.gongsimchae.domain.QBaseEntity _super = new techit.gongsimchae.domain.QBaseEntity(this);

    public final techit.gongsimchae.domain.portion.chatroom.entity.QChatRoom chatRoom;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final techit.gongsimchae.domain.groupbuying.event.entity.QEvent event;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> imageFileStatus = createNumber("imageFileStatus", Integer.class);

    public final techit.gongsimchae.domain.common.inquiry.entity.QInquiry inquiry;

    public final techit.gongsimchae.domain.groupbuying.item.entity.QItem item;

    public final StringPath originalFilename = createString("originalFilename");

    public final techit.gongsimchae.domain.groupbuying.post.entity.QPost post;

    public final techit.gongsimchae.domain.groupbuying.reviews.entity.QReviews reviews;

    public final StringPath storeFilename = createString("storeFilename");

    public final techit.gongsimchae.domain.portion.subdivision.entity.QSubdivision subdivision;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final techit.gongsimchae.domain.common.user.entity.QUser user;

    public QImageFile(String variable) {
        this(ImageFile.class, forVariable(variable), INITS);
    }

    public QImageFile(Path<? extends ImageFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QImageFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QImageFile(PathMetadata metadata, PathInits inits) {
        this(ImageFile.class, metadata, inits);
    }

    public QImageFile(Class<? extends ImageFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new techit.gongsimchae.domain.portion.chatroom.entity.QChatRoom(forProperty("chatRoom"), inits.get("chatRoom")) : null;
        this.event = inits.isInitialized("event") ? new techit.gongsimchae.domain.groupbuying.event.entity.QEvent(forProperty("event")) : null;
        this.inquiry = inits.isInitialized("inquiry") ? new techit.gongsimchae.domain.common.inquiry.entity.QInquiry(forProperty("inquiry"), inits.get("inquiry")) : null;
        this.item = inits.isInitialized("item") ? new techit.gongsimchae.domain.groupbuying.item.entity.QItem(forProperty("item"), inits.get("item")) : null;
        this.post = inits.isInitialized("post") ? new techit.gongsimchae.domain.groupbuying.post.entity.QPost(forProperty("post"), inits.get("post")) : null;
        this.reviews = inits.isInitialized("reviews") ? new techit.gongsimchae.domain.groupbuying.reviews.entity.QReviews(forProperty("reviews"), inits.get("reviews")) : null;
        this.subdivision = inits.isInitialized("subdivision") ? new techit.gongsimchae.domain.portion.subdivision.entity.QSubdivision(forProperty("subdivision"), inits.get("subdivision")) : null;
        this.user = inits.isInitialized("user") ? new techit.gongsimchae.domain.common.user.entity.QUser(forProperty("user")) : null;
    }

}

