package techit.gongsimchae.domain.portion.areas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.portion.areas.entity.SigunguArea;

import java.util.List;

public interface SigunguAreaRepository extends JpaRepository<SigunguArea, Long> {

    List<SigunguArea> findBySidoAreaId(Long sidoAreaId);
}
