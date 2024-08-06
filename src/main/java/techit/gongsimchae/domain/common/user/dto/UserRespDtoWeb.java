package techit.gongsimchae.domain.common.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.common.address.dto.AddressRespDtoWeb;
import techit.gongsimchae.domain.common.user.entity.JoinType;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.entity.UserRole;
import techit.gongsimchae.domain.common.user.entity.UserStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRespDtoWeb {
    private Long id;
    private String name;
    private int mannerPoint;
    private String email;
    private String loginId;
    private String nickname;
    private String phoneNumber;
    private String UID;
    private UserStatus userStatus;
    private UserRole role;
    private String password;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private JoinType joinType;
    private String passwordChange;
    private String passwordChangeConfirm;

    /**
     * 유저정보가 다 담기는 생성자
     *
     */
    public UserRespDtoWeb(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.loginId = user.getLoginId();
        this.nickname = user.getNickname();
        this.phoneNumber = user.getPhoneNumber();
        this.createDate = user.getCreateDate();
        this.updateDate = user.getUpdateDate();
        this.role = user.getRole();
        this.userStatus = user.getUserStatus();
        this.UID = user.getUID();
        this.mannerPoint = user.getMannerPoint();
        this.joinType = user.getJoinType();

    }
}
