package techit.gongsimchae.domain.portion.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.portion.feedback.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback,Long> {
}
