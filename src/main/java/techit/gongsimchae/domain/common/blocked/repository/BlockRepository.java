package techit.gongsimchae.domain.common.blocked.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.common.blocked.entity.Block;

public interface BlockRepository extends JpaRepository<Block, Long> {
}
