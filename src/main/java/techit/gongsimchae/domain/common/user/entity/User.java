package techit.gongsimchae.domain.common.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.Address;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.common.blocked.entity.Block;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.user.dto.UserAdminUpdateReqDtoWeb;
import techit.gongsimchae.domain.common.user.dto.UserJoinReqDtoWeb;
import techit.gongsimchae.domain.common.user.dto.UserUpdateReqDtoWeb;
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
    @Column(unique = true)
    private String phoneNumber;
    private Integer mannerPoint;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @Column(unique = true)
    private String UID;
    private JoinType joinType;

    @Embedded
    private Address address;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_file_id")
    private ImageFile imageFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_id")
    private Block block;

    /**
     * 생성자
     * 일반 회원가입
     */

    public User(UserJoinReqDtoWeb joinReqDto, Address address) {
        this.name = joinReqDto.getName();
        this.email = joinReqDto.getEmail();
        this.password = joinReqDto.getPassword();
        this.loginId = joinReqDto.getLoginId();
        this.role = UserRole.ROLE_USER;
        this.nickname = joinReqDto.getNickname();
        this.phoneNumber = joinReqDto.getPhoneNumber();
        this.userStatus = UserStatus.NORMAL;
        this.UID = UUID.randomUUID().toString();
        this.address = address;
        this.mannerPoint = 0;
        this.joinType = JoinType.NORMAL;
    }

    /**
     * OAuth2로 로그인시 DB에 저장할것들
     */

    public User(OAuth2Response oAuth2Response, Address address) {
        this.name = oAuth2Response.getName();
        this.loginId = oAuth2Response.getLoginId();
        this.role = UserRole.ROLE_USER;
        this.email = oAuth2Response.getEmail();
        this.nickname = UUID.randomUUID().toString().substring(0, 8);
        this.userStatus = UserStatus.NORMAL;
        this.UID = UUID.randomUUID().toString();
        this.address = address;
        this.mannerPoint = 0;
        this.joinType = JoinType.OAUTH;

    }



    /**
     * 비지니스 메서드
     */

    /**
     * 기존 Oauth2 회원이 또 로그인헀을 때 바뀐게 있는지 확인
     */

    public void changeOauth(OAuth2Response oAuth2Response) {
         this.name = oAuth2Response.getName();
         this.loginId = oAuth2Response.getLoginId();
         this.email = oAuth2Response.getEmail();
    }

    /**
     * 개인정보 수정으로 바뀌는 정보들
     */

    public void changeInfo(UserUpdateReqDtoWeb userUpdateReqDtoWeb) {
        this.nickname = userUpdateReqDtoWeb.getNickname();
        this.phoneNumber = userUpdateReqDtoWeb.getPhoneNumber();
        this.email = userUpdateReqDtoWeb.getEmail();
        this.password = userUpdateReqDtoWeb.getPasswordChange();
    }


    /**
     * 자신 email인지 확인
     */
    public boolean isUserEmail(String email) {
        return this.email.equals(email);
    }

    /**
     * 자신 phoneNumber 확인
     */
    public boolean isUserPhoneNumber(String phoneNumber) {
        return this.phoneNumber.equals(phoneNumber);
    }

    public boolean isUserNickname(String nickname) {
        return this.nickname.equals(nickname);
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }


    public void changeInfoByAdmin(UserAdminUpdateReqDtoWeb updateDto, Address address) {
        this.name = updateDto.getName();
        this.loginId = updateDto.getLoginId();
        this.role = updateDto.getRole();
        this.email = updateDto.getEmail();
        this.nickname = updateDto.getNickname();
        this.phoneNumber = updateDto.getPhoneNumber();
        this.userStatus = updateDto.getUserStatus();
        this.address = address;
        this.mannerPoint = updateDto.getMannerPoint();
        this.joinType = updateDto.getJoinType();

    }
}
