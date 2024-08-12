package techit.gongsimchae.domain.portion.subdivision.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubdivisionRepository extends JpaRepository<Subdivision, Long> {

    @Query("SELECT s FROM Subdivision s LEFT JOIN FETCH s.imageFileList WHERE s.UID = :uid")
    Optional<Subdivision> findByUID(@Param("uid") String UID);

    @Query("SELECT s FROM Subdivision s LEFT JOIN FETCH s.imageFileList")
    List<Subdivision> findByOrderByCreateDateDesc();

    List<Subdivision> findAllByUserId(Long userId);
}
