package techit.gongsimchae.domain.common.refreshtoken.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    @Column(length = 512)
    private String refreshToken;
    private String expiration;

    public RefreshTokenEntity(String loginId, String refreshToken, String expiration) {
        this.loginId = loginId;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }
}
