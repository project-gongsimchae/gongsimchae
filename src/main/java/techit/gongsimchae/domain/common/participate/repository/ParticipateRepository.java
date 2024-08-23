package techit.gongsimchae.domain.common.participate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.common.participate.entity.Participate;

public interface ParticipateRepository extends JpaRepository<Participate, Long> {
}
