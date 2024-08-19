package techit.gongsimchae.domain.portion.subdivision.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionChatRoomRespDto;

import java.util.List;
import java.util.stream.Collectors;

import static techit.gongsimchae.domain.common.imagefile.entity.QImageFile.imageFile;
import static techit.gongsimchae.domain.common.user.entity.QUser.user;
import static techit.gongsimchae.domain.portion.chatroom.entity.QChatRoom.chatRoom;
import static techit.gongsimchae.domain.portion.chatroomuser.entity.QChatRoomUser.chatRoomUser;
import static techit.gongsimchae.domain.portion.subdivision.entity.QSubdivision.subdivision;

@RequiredArgsConstructor
public class SubdivisionCustomRepositoryImpl implements SubdivisionCustomRepository {
    private final JPAQueryFactory queryFactory;
    @Override
    public List<SubdivisionChatRoomRespDto> findUserSubdivisions(Long userId) {
        // 기본 데이터 조회 쿼리
        List<SubdivisionChatRoomRespDto> subdivisions = queryFactory.select(Projections.fields(SubdivisionChatRoomRespDto.class,
                        subdivision.id.as("subdivisionId"), subdivision.title.as("subdivisionTitle"),
                        subdivision.address, subdivision.createDate, subdivision.price, subdivision.updateDate,
                        subdivision.subdivisionType, subdivision.UID.as("subdivisionUID"),
                        chatRoom.maxUserCnt, chatRoom.roomId))
                .from(chatRoomUser)
                .join(chatRoomUser.user, user)
                .join(chatRoomUser.chatRoom, chatRoom)
                .join(chatRoom.subdivision, subdivision)
                .where(user.id.eq(userId))
                .fetch();

        for (SubdivisionChatRoomRespDto subdivisionChatRoomRespDto : subdivisions) {
            List<ImageFile> imageFiles = queryFactory.select(imageFile)
                    .from(imageFile)
                    .join(imageFile.subdivision, subdivision)
                    .where(subdivision.id.eq(subdivisionChatRoomRespDto.getSubdivisionId()))
                    .fetch();

            subdivisionChatRoomRespDto.setSubdivisionImages(imageFiles);
        }

        // 크기 계산을 위한 후처리
        return subdivisions.stream().map(dto -> {
            String roomId = dto.getRoomId();// 적절히 ChatRoom ID를 가져오는 로직 필요
            Long chatRoomUserCount = queryFactory.select(chatRoomUser.count())
                    .from(chatRoomUser)
                    .where(chatRoomUser.chatRoom.roomId.eq(roomId))
                    .fetchOne();
            dto.setChatRoomUsers(chatRoomUserCount);
            return dto;
        }).collect(Collectors.toList());

    }
}
