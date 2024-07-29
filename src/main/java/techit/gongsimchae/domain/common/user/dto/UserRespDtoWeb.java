package techit.gongsimchae.domain.common.user.dto;

import lombok.Data;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.entity.UserRole;
import techit.gongsimchae.domain.common.user.entity.UserStatus;

@Data
public class UserRespDtoWeb {

    private String name;
    private String email;
    private String loginId;
    private String nickname;
    private String phoneNumber;
    private String UID;
    private String zipcode;
    private String address;
    private String detailAddress;
    private UserStatus userStatus;
    private UserRole role;
    private String password;

    private String changePassword;
    private String changePasswordConfirm;

    /**
     * 유저가 자기 자신 정보 조회할때 쓰는 생성자
     *
     */
    public UserRespDtoWeb(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.loginId = user.getLoginId();
        this.nickname = user.getNickname();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress().getAddress();
        this.detailAddress = user.getAddress().getDetailAddress();
        this.password = user.getPassword();
    }
}
