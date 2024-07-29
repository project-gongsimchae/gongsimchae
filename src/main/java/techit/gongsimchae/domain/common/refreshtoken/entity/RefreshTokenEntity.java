package techit.gongsimchae.domain.common.refreshtoken.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.TimeToLive;

@Entity
@Getter
@NoArgsConstructor
public class RefreshTokenEntity {

    @Id
    private String id;

    private String loginId;
    @Column(length = 512)
    private String refreshToken;
    private String expiration;
    @TimeToLive
    private long ttl;

    public RefreshTokenEntity(String loginId, String refreshToken, String expiration) {
        this.loginId = loginId;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }
}
