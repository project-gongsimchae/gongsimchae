package techit.gongsimchae.domain.portion.subdivision.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubdivisionRepository extends JpaRepository<Subdivision, Long>, SubdivisionCustomRepository {

    Optional<Subdivision> findByUID(String UID);

    List<Subdivision> findByDeleteStatusIsFalseOrderByCreateDateDesc();

    List<Subdivision> findAllByUserIdAndDeleteStatusIsFalse(Long userId);
}
