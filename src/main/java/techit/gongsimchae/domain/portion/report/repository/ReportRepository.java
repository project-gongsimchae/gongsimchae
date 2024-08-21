package techit.gongsimchae.domain.portion.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.portion.report.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
