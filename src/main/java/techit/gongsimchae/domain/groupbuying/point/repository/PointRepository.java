package techit.gongsimchae.domain.groupbuying.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.point.entity.Point;

public interface PointRepository extends JpaRepository<Point,Long> {
}
