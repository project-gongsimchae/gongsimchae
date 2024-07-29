package techit.gongsimchae.domain.common.user.dto;

import lombok.Data;
import techit.gongsimchae.global.valid.UserUpdateUnique;

@Data
public class UserUpdateReqDtoWeb {
    private String loginId;
    private String password;
    @UserUpdateUnique
    private String email;
    @UserUpdateUnique
    private String nickname;
    @UserUpdateUnique
    private String phoneNumber;
    private String changePassword;
    private String changePasswordConfirm;
}
