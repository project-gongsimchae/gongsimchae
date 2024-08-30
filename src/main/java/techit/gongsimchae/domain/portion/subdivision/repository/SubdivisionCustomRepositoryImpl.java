package techit.gongsimchae.domain.portion.subdivision.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import techit.gongsimchae.domain.common.imagefile.dto.ImageFileRespDto;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.portion.subdivision.dto.SubSearchDto;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionChatRoomRespDto;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionReportRespDto;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionRespDto;
import techit.gongsimchae.domain.portion.subdivision.entity.SubdivisionType;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static techit.gongsimchae.domain.common.imagefile.entity.QImageFile.imageFile;
import static techit.gongsimchae.domain.common.user.entity.QUser.user;
import static techit.gongsimchae.domain.portion.chatroom.entity.QChatRoom.chatRoom;
import static techit.gongsimchae.domain.portion.chatroomuser.entity.QChatRoomUser.chatRoomUser;
import static techit.gongsimchae.domain.portion.report.entity.QReport.report;
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
                .where(user.id.eq(userId).and(subdivision.deleteStatus.eq(false)))
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

    @Override
    public Page<SubdivisionReportRespDto> findMostFrequentReports(Pageable pageable) {

        // 1. 서브쿼리 실행: 가장 자주 보고된 구역 ID와 카운트를 가져옴
        List<SubdivisionReportRespDto> subQueryResults = queryFactory
                .select(Projections.fields(SubdivisionReportRespDto.class,
                        subdivision.id.as("subdivisionId"), report.count().as("reportCount")))
                .from(report)
                .join(report.subdivision, subdivision)
                .where(subdivision.deleteStatus.eq(false))
                .groupBy(subdivision.id)
                .fetch();

        // 2. 서브쿼리 결과에서 subdivisionId 목록을 추출
        List<Long> subdivisionIds = subQueryResults.stream()
                .map(SubdivisionReportRespDto::getSubdivisionId)
                .collect(Collectors.toList());

        // 3. 메인 쿼리 실행: 서브쿼리에서 추출한 ID로 필터링하여 상세 정보 가져옴
        List<SubdivisionReportRespDto> results = queryFactory
                .select(Projections.fields(SubdivisionReportRespDto.class,
                        subdivision.id.as("subdivisionId"), subdivision.title, user.email, user.nickname,
                        report.count().as("reportCount")))
                .from(report)
                .join(report.subdivision, subdivision)
                .join(subdivision.user, user)
                .where(subdivision.id.in(subdivisionIds))
                .groupBy(subdivision.id, user.email, user.nickname)
                .orderBy(report.count().desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Long size = queryFactory.select(subdivision.countDistinct())
                .from(report)
                .join(report.subdivision, subdivision)
                .where(subdivision.deleteStatus.eq(false))
                .fetchOne();

        // 4. 결과를 Page로 래핑하여 반환
        return new PageImpl<>(results, pageable,size);
    }

    @Override
    public Page<SubdivisionRespDto> searchAndSortSubdivisions(SubSearchDto searchDto, Pageable pageable) {
        List<SubdivisionRespDto> results = queryFactory.select(Projections.fields(SubdivisionRespDto.class, subdivision.id, subdivision.title,
                        subdivision.content, subdivision.address, subdivision.price, subdivision.views,
                        subdivision.UID, subdivision.subdivisionType, subdivision.createDate, subdivision.updateDate))
                .from(subdivision)
                .where(subdivision.deleteStatus.eq(false).and(AllConditions(searchDto)))
                .orderBy(sortType(searchDto))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        for (SubdivisionRespDto result : results) {
            List<ImageFileRespDto> images = queryFactory.select(Projections.fields(ImageFileRespDto.class,
                            imageFile.id, imageFile.originalFilename, imageFile.storeFilename))
                    .from(imageFile)
                    .join(imageFile.subdivision, subdivision)
                    .where(subdivision.id.eq(result.getId()))
                    .fetch();
            result.setImageFileList(images);
        }

        Long size = queryFactory.select(subdivision.count())
                .from(subdivision)
                .where(subdivision.deleteStatus.eq(false).and(AllConditions(searchDto)))
                .fetchOne();


        return new PageImpl<>(results, pageable,size.intValue());

    }

    private OrderSpecifier<?> sortType(SubSearchDto searchDto) {
        if (searchDto.getSort() != null) {
            if (searchDto.getSort().equals("new")) {
                return subdivision.createDate.desc();
            } else if (searchDto.getSort().equals("popular")) {
                return subdivision.views.desc();
            }

        }
        return subdivision.createDate.desc();
    }

    private BooleanBuilder AllConditions(SubSearchDto searchDto) {
        BooleanBuilder builder = new BooleanBuilder();
        if (Objects.nonNull(searchDto.getOnSale())) {
            builder.and(subdivision.subdivisionType.eq(SubdivisionType.RECRUITING));
        }
        if(searchDto.getAddress() != null && !searchDto.getAddress().isBlank()) {
            String[] split = searchDto.getAddress().split(",");
            builder.and(subdivision.dong.in(split));
        }
        if (searchDto.getContent() != null) {
            builder.and(subdivision.title.contains(searchDto.getContent())
                    .and(subdivision.content.contains(searchDto.getContent())));
        }
        return builder;
    }
}
