package techit.gongsimchae.domain.common.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.common.user.dto.UserAdminUpdateReqDtoWeb;
import techit.gongsimchae.domain.common.user.dto.UserJoinReqDtoWeb;
import techit.gongsimchae.domain.common.user.dto.UserUpdateReqDtoWeb;
import techit.gongsimchae.global.dto.OAuth2Response;

import java.time.LocalDateTime;
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
    private Double mannerPoint;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @Column(unique = true)
    private String UID;
    @Enumerated(EnumType.STRING)
    private JoinType joinType;

    private LocalDateTime deletedAt;

    /**
     * 생성자
     * 일반 회원가입
     */

    public User(UserJoinReqDtoWeb joinReqDto) {
        this.name = joinReqDto.getName();
        this.email = joinReqDto.getEmail();
        this.password = joinReqDto.getPassword();
        this.loginId = joinReqDto.getLoginId();
        this.role = UserRole.ROLE_USER;
        this.nickname = joinReqDto.getNickname();
        this.phoneNumber = joinReqDto.getPhoneNumber();
        this.userStatus = UserStatus.NORMAL;
        this.UID = UUID.randomUUID().toString();
        this.mannerPoint = 36.5;
        this.joinType = JoinType.NORMAL;
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
        this.mannerPoint = 36.5;
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

    /**
     * 관리자가 유저정보 수정할 때 수정하는 것들
     */
    public void changeInfoByAdmin(UserAdminUpdateReqDtoWeb updateDto) {
        this.name = updateDto.getName();
        this.role = updateDto.getRole();
        this.email = updateDto.getEmail();
        this.nickname = updateDto.getNickname();
        this.phoneNumber = updateDto.getPhoneNumber();
        this.userStatus = updateDto.getUserStatus();
        this.mannerPoint = updateDto.getMannerPoint();
        this.joinType = updateDto.getJoinType();

    }

    public void ban() {
        this.userStatus = UserStatus.PENALTY;
    }

    /**
     *  매너포인트 조절
     */
    public void decrementMannerPoints() {
        if (this.mannerPoint > 0.0) {
            this.mannerPoint -= 0.1;
        }
    }

    public void increaseMannerPointsForBestFeedback() {
        if (this.mannerPoint < 99.9) {
            this.mannerPoint += 0.2;
        }
    }

    public void increaseMannerPointsForGoodFeedback() {
        if (this.mannerPoint < 100.0) {
            this.mannerPoint += 0.1;
        }
    }

    /**
     * 삭제 처리
     */
    public void deleteUser(){
        this.deletedAt = LocalDateTime.now();
    }
}
