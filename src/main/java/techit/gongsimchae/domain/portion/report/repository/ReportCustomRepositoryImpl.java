package techit.gongsimchae.domain.portion.report.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import techit.gongsimchae.domain.portion.report.dto.ReportRespDtoWeb;

import java.util.List;

import static techit.gongsimchae.domain.common.user.entity.QUser.user;
import static techit.gongsimchae.domain.portion.report.entity.QReport.report;
import static techit.gongsimchae.domain.portion.subdivision.entity.QSubdivision.subdivision;

@RequiredArgsConstructor
public class ReportCustomRepositoryImpl implements ReportCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ReportRespDtoWeb> findReportsForSubdivision(Long subdivisionId) {
        return queryFactory.select(Projections.fields(ReportRespDtoWeb.class, report.id,
                        report.title, report.content, report.reportType, user.id.as("userId"),
                        subdivision.UID.as("subdivisionUID")))
                .from(report)
                .join(report.subdivision, subdivision)
                .join(subdivision.user, user)
                .where(subdivision.id.eq(subdivisionId))
                .fetch();
    }
}
