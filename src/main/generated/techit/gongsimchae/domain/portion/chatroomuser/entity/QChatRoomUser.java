package techit.gongsimchae.domain.portion.chatroomuser.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoomUser is a Querydsl query type for ChatRoomUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoomUser extends EntityPathBase<ChatRoomUser> {

    private static final long serialVersionUID = 1204083054L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatRoomUser chatRoomUser = new QChatRoomUser("chatRoomUser");

    public final techit.gongsimchae.domain.QBaseEntity _super = new techit.gongsimchae.domain.QBaseEntity(this);

    public final techit.gongsimchae.domain.portion.chatroom.entity.QChatRoom chatRoom;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> isActivate = createNumber("isActivate", Integer.class);

    public final NumberPath<Integer> isNotifications = createNumber("isNotifications", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final techit.gongsimchae.domain.common.user.entity.QUser user;

    public QChatRoomUser(String variable) {
        this(ChatRoomUser.class, forVariable(variable), INITS);
    }

    public QChatRoomUser(Path<? extends ChatRoomUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatRoomUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatRoomUser(PathMetadata metadata, PathInits inits) {
        this(ChatRoomUser.class, metadata, inits);
    }

    public QChatRoomUser(Class<? extends ChatRoomUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new techit.gongsimchae.domain.portion.chatroom.entity.QChatRoom(forProperty("chatRoom"), inits.get("chatRoom")) : null;
        this.user = inits.isInitialized("user") ? new techit.gongsimchae.domain.common.user.entity.QUser(forProperty("user")) : null;
    }

}

