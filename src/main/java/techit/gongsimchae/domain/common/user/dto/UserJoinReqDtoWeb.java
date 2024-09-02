package techit.gongsimchae.domain.common.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import techit.gongsimchae.global.valid.JoinUnique;
import techit.gongsimchae.global.valid.NicknameUnique;

@Data
public class UserJoinReqDtoWeb {
    @NotEmpty
    private String name;
    @JoinUnique
    private String email;
    @Pattern(
            regexp = "(?=.*[!@#$%]).{8,20}",
            message = "8~20자 사이의 비밀번호는 한글, 영어, 숫자, 특수문자를 포함해야합니다. (!@#$%)"
    )
    private String password;
    @NotEmpty
    private String passwordConfirm;
    @JoinUnique
    private String loginId;
    @NicknameUnique
    private String nickname;
    @JoinUnique
    private String phoneNumber;
    @NotEmpty
    private String AuthCode;

}
