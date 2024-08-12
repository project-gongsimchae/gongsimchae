package techit.gongsimchae.domain.common.inquiry.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import techit.gongsimchae.domain.common.inquiry.dto.InquiryRespDtoWeb;
import techit.gongsimchae.domain.common.inquiry.dto.InquirySearchForm;

import java.util.List;

import static techit.gongsimchae.domain.common.inquiry.entity.QInquiry.inquiry;
import static techit.gongsimchae.domain.common.user.entity.QUser.user;

@RequiredArgsConstructor
public class InquiryCustomRepositoryImpl implements InquiryCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<InquiryRespDtoWeb> findAllInquiries(Pageable pageable, String filter) {
        List<InquiryRespDtoWeb> result = queryFactory.select(Projections.fields(InquiryRespDtoWeb.class,
                        inquiry.id, inquiry.title, inquiry.content, inquiry.inquiryType, inquiry.isAnswered,
                        inquiry.createDate, inquiry.updateDate, inquiry.UID, inquiry.answer,
                        user.name, user.nickname, user.email))
                .from(inquiry)
                .join(inquiry.user, user)
                .orderBy(inquiry.createDate.desc())
                .where(unanswered(filter))
                .fetch();

        int total = queryFactory.select(inquiry)
                .from(inquiry)
                .join(inquiry.user, user)
                .where(unanswered(filter))
                .fetch().size();
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public InquiryRespDtoWeb findInquiryByUID(String id) {
        return queryFactory.select(Projections.fields(InquiryRespDtoWeb.class,
                inquiry.id, inquiry.title, inquiry.content, inquiry.inquiryType, inquiry.isAnswered,
                inquiry.createDate, inquiry.updateDate, inquiry.UID, inquiry.answer,
                user.name, user.nickname, user.email))
                .from(inquiry)
                .join(inquiry.user, user)
                .where(inquiry.UID.eq(id))
                .fetchOne();
    }

    private BooleanExpression unanswered(String filter) {
        return filter.equals("all") ? null : filter.equals("unanswered") ? inquiry.isAnswered.eq(0) : inquiry.isAnswered.eq(1);
    }
}
