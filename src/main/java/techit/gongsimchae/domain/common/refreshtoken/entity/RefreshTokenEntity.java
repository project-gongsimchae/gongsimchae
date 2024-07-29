package techit.gongsimchae.domain.common.refreshtoken.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Index;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Getter
@NoArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 60 * 60* 24 * 7)
public class RefreshTokenEntity {
    @Id
    private String id;

    private String loginId;
    @Indexed
    private String refreshToken;

    public RefreshTokenEntity(String loginId, String refreshToken) {
        id = UUID.randomUUID().toString();
        this.loginId = loginId;
        this.refreshToken = refreshToken;
    }
}

