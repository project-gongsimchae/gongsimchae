package techit.gongsimchae.domain.portion.areas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.portion.areas.entity.MyeondongeupArea;

import java.util.List;

public interface MyeondongeupAreaRepository extends JpaRepository<MyeondongeupArea, Long> {
    List<MyeondongeupArea> findBySigunguAreaId(Long sigunguAreaId);
}
