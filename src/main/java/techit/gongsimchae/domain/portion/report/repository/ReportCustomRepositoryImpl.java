package techit.gongsimchae.domain.portion.report.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import techit.gongsimchae.domain.portion.report.dto.ReportRespDtoWeb;

import java.util.List;

import static techit.gongsimchae.domain.common.user.entity.QUser.user;
import static techit.gongsimchae.domain.portion.report.entity.QReport.report;
import static techit.gongsimchae.domain.portion.subdivision.entity.QSubdivision.subdivision;

@RequiredArgsConstructor
public class ReportCustomRepositoryImpl implements ReportCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReportRespDtoWeb> findReportsForSubdivision(Long subdivisionId, Pageable pageable) {
        List<ReportRespDtoWeb> results = queryFactory.select(Projections.fields(ReportRespDtoWeb.class, report.id,
                        report.title, report.content, report.reportType, user.id.as("userId"),
                        subdivision.UID.as("subdivisionUID")))
                .from(report)
                .join(report.subdivision, subdivision)
                .join(subdivision.user, user)
                .where(subdivision.id.eq(subdivisionId))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(report.createDate.desc())
                .fetch();

        int size = queryFactory.select(report)
                .from(report)
                .join(report.subdivision, subdivision)
                .where(subdivision.id.eq(subdivisionId))
                .fetch().size();

        return new PageImpl<>(results, pageable, size);
    }
}
