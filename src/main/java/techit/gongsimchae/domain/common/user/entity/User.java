package techit.gongsimchae.domain.common.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.Address;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.common.user.dto.UserJoinReqDto;
import techit.gongsimchae.global.dto.OAuth2Response;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "users")
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String name;
    private String password;
    @Column(unique = true)
    private String loginId;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String nickname;
    private String phoneNumber;
    private Integer mannerPoint;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @Column(unique = true)
    private String UID;

    @Embedded
    private Address address;

    /**
     * 생성자
     * 일반 회원가입
     */

    public User(UserJoinReqDto joinReqDto) {

        this.name = joinReqDto.getName();
        this.email = joinReqDto.getEmail();
        this.password = joinReqDto.getPassword();
        this.loginId = joinReqDto.getLoginId();
        this.role = UserRole.ROLE_USER;
        this.nickname = joinReqDto.getNickname();
        this.phoneNumber = joinReqDto.getPhoneNumber();
        this.userStatus = UserStatus.NORMAL;
        this.UID = UUID.randomUUID().toString();
    }

    /**
     * OAuth2로 로그인시 DB에 저장할것들
     */

    public User(OAuth2Response oAuth2Response) {
        this.name = oAuth2Response.getName();
        this.loginId = oAuth2Response.getLoginId();
        this.role = UserRole.ROLE_USER;
        this.email = oAuth2Response.getEmail();
        this.nickname = UUID.randomUUID().toString().substring(0, 8);
        this.userStatus = UserStatus.NORMAL;
        this.UID = UUID.randomUUID().toString();
    }

    public void changeOauth(OAuth2Response oAuth2Response) {
         this.name = oAuth2Response.getName();
         this.loginId = oAuth2Response.getLoginId();
         this.email = oAuth2Response.getEmail();
    }
}
