package techit.gongsimchae.domain.portion.chatroom.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoom is a Querydsl query type for ChatRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoom extends EntityPathBase<ChatRoom> {

    private static final long serialVersionUID = 1830388024L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatRoom chatRoom = new QChatRoom("chatRoom");

    public final ListPath<techit.gongsimchae.domain.portion.chatroomuser.entity.ChatRoomUser, techit.gongsimchae.domain.portion.chatroomuser.entity.QChatRoomUser> chatRoomUsers = this.<techit.gongsimchae.domain.portion.chatroomuser.entity.ChatRoomUser, techit.gongsimchae.domain.portion.chatroomuser.entity.QChatRoomUser>createList("chatRoomUsers", techit.gongsimchae.domain.portion.chatroomuser.entity.ChatRoomUser.class, techit.gongsimchae.domain.portion.chatroomuser.entity.QChatRoomUser.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> maxUserCnt = createNumber("maxUserCnt", Integer.class);

    public final StringPath roomId = createString("roomId");

    public final StringPath roomName = createString("roomName");

    public final techit.gongsimchae.domain.portion.subdivision.entity.QSubdivision subdivision;

    public QChatRoom(String variable) {
        this(ChatRoom.class, forVariable(variable), INITS);
    }

    public QChatRoom(Path<? extends ChatRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatRoom(PathMetadata metadata, PathInits inits) {
        this(ChatRoom.class, metadata, inits);
    }

    public QChatRoom(Class<? extends ChatRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.subdivision = inits.isInitialized("subdivision") ? new techit.gongsimchae.domain.portion.subdivision.entity.QSubdivision(forProperty("subdivision"), inits.get("subdivision")) : null;
    }

}

