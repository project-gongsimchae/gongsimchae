package techit.gongsimchae.domain.common.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import techit.gongsimchae.global.valid.UserUpdateUnique;

@Data
public class UserUpdateReqDtoWeb {

    @UserUpdateUnique
    private String email;
    @UserUpdateUnique
    private String nickname;
    @UserUpdateUnique
    private String phoneNumber;
    @Pattern(
            regexp = "(?=.*[!@#$%]).{8,20}",
            message = "8~20자 사이의 비밀번호는 한글, 영어, 숫자, 특수문자를 포함해야합니다. (!@#$%)"
    )
    private String password;
    @Pattern(
            regexp = "(?=.*[!@#$%]).{8,20}",
            message = "8~20자 사이의 비밀번호는 한글, 영어, 숫자, 특수문자를 포함해야합니다. (!@#$%)"
    )
    private String passwordChange;
    @NotEmpty
    private String passwordChangeConfirm;
}
