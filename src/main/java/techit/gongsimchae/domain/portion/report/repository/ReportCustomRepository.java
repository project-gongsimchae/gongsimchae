package techit.gongsimchae.domain.portion.report.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import techit.gongsimchae.domain.portion.report.dto.ReportRespDtoWeb;

public interface ReportCustomRepository {
    Page<ReportRespDtoWeb> findReportsForSubdivision(Long subdivisionId, Pageable pageable);
}
