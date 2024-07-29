package techit.gongsimchae.domain.common.refreshtoken.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
import techit.gongsimchae.global.security.jwt.JwtVO;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@RedisHash("refreshTokenEntity")
@Getter
@NoArgsConstructor
@ToString
public class RefreshTokenEntity implements Serializable {

    @Id
    private String id;

    private String loginId;
    @Column(length = 512)
    @Indexed
    private String refreshToken;
    @TimeToLive
    private Long ttl;

    public RefreshTokenEntity(String loginId, String refreshToken) {
        this.id = UUID.randomUUID().toString();
        this.loginId = loginId;
        this.refreshToken = refreshToken;
        this.ttl =  JwtVO.REFRESH_TOKEN_EXPIRES_TIME;
    }
}
