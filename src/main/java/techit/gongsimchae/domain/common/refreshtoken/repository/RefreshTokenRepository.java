package techit.gongsimchae.domain.common.refreshtoken.repository;

import org.springframework.data.repository.CrudRepository;
import techit.gongsimchae.domain.common.refreshtoken.entity.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, String> {
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);


}
