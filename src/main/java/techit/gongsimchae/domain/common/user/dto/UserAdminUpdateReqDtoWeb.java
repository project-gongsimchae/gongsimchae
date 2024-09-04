package techit.gongsimchae.domain.common.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import techit.gongsimchae.domain.common.user.entity.JoinType;
import techit.gongsimchae.domain.common.user.entity.UserRole;
import techit.gongsimchae.domain.common.user.entity.UserStatus;
import techit.gongsimchae.global.valid.UserUpdateUnique;

@Data
public class UserAdminUpdateReqDtoWeb {
    private Long id;
    @NotEmpty
    private String name;
    private Double mannerPoint;
    @UserUpdateUnique
    private String email;

    @UserUpdateUnique
    private String nickname;
    @UserUpdateUnique
    private String phoneNumber;
    @NotNull
    private UserStatus userStatus;
    @NotNull
    private UserRole role;
    @NotNull
    private JoinType joinType;

    // 주소

    private String zipcode;

    private String address;

    private String detailAddress;

    private String additionalAddress;
    private String sigungu;

}
