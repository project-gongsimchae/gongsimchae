package techit.gongsimchae.domain.groupbuying.reviews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.reviews.entity.Reviews;

public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
}
