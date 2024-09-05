package techit.gongsimchae.domain.common.participate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import techit.gongsimchae.domain.common.participate.entity.Participate;

import java.util.Optional;

public interface ParticipateRepository extends JpaRepository<Participate, Long> {
    @Query("select count(*) from Participate  p where p.item.id = :id")
    Long countByItem(@Param("id") Long id);

    @Query("select p from Participate p join p.item i join p.user u where i.id = :id and u.loginId = :loginId")
    Optional<Participate> findParticipate(@Param("id") Long itemId, @Param("loginId") String loginId);
}
