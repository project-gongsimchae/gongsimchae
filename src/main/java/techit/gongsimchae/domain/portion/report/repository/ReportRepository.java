package techit.gongsimchae.domain.portion.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.portion.report.entity.Report;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;

public interface ReportRepository extends JpaRepository<Report, Long>, ReportCustomRepository {
    void deleteAllBySubdivisionUID(String subdivisionUID);

    Long countBySubdivision(Subdivision subdivision);
}
