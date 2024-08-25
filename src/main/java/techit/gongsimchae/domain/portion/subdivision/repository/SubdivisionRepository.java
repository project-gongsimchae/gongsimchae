package techit.gongsimchae.domain.portion.subdivision.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubdivisionRepository extends JpaRepository<Subdivision, Long>, SubdivisionCustomRepository {

    Optional<Subdivision> findByUID(String UID);

    List<Subdivision> findByDeleteStatusIsFalseOrderByCreateDateDesc();

    List<Subdivision> findAllByUserIdAndDeleteStatusIsFalse(Long userId);

    @Query("SELECT s FROM Subdivision s WHERE s.deleteStatus = false " +
            "AND (:address IS NULL OR " +
            "   LOWER(s.address) LIKE LOWER(CONCAT('%', :address, '%')) OR " +
            "   LOWER(s.address) LIKE LOWER(CONCAT('%', REPLACE(:address, ' ', '%'), '%'))) " +
            "AND (:content IS NULL OR LOWER(s.content) LIKE LOWER(CONCAT('%', :content, '%')) OR LOWER(s.title) LIKE LOWER(CONCAT('%', :content, '%')))")
    List<Subdivision> searchSubdivisions(@Param("address") String address, @Param("content") String content);

    @Query("update Subdivision s set s.views = s.views + :views where s.UID = :uid")
    @Modifying(clearAutomatically = true)
    void updateViewCount(@Param("views") int viewCount, @Param("uid") String subdivisionId);
}
