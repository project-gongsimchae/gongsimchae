package techit.gongsimchae.domain.common.user.dto;

import lombok.Data;

@Data
public class UserUpdateReqDtoWeb {
    private String loginId;
    private String password;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String changePassword;
    private String changePasswordConfirm;
}
