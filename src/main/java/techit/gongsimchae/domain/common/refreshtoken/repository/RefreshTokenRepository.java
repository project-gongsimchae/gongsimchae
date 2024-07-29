package techit.gongsimchae.domain.common.refreshtoken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.refreshtoken.entity.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, String> {
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

    @Transactional
    void deleteByRefreshToken(String refreshToken);

    Boolean existsByRefreshToken(String refreshToken);
}
