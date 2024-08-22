package techit.gongsimchae.domain.common.user.dto;

import lombok.Data;
import techit.gongsimchae.domain.common.user.entity.JoinType;
import techit.gongsimchae.domain.common.user.entity.UserRole;
import techit.gongsimchae.domain.common.user.entity.UserStatus;
@Data
public class UserAdminUpdateReqDtoWeb {
    private Long id;
    private String name;
    private int mannerPoint;
    private String email;
    private String loginId;
    private String nickname;
    private String phoneNumber;
    private UserStatus userStatus;
    private UserRole role;
    private JoinType joinType;

    // 주소
    private String zipcode;
    private String address;
    private String detailAddress;
    private String additionalAddress;

}
