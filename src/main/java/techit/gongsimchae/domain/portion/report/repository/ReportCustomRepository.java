package techit.gongsimchae.domain.portion.report.repository;

import techit.gongsimchae.domain.portion.report.dto.ReportRespDtoWeb;

import java.util.List;

public interface ReportCustomRepository {
    List<ReportRespDtoWeb> findReportsForSubdivision(Long subdivisionId);
}
