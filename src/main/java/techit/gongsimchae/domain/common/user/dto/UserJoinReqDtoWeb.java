package techit.gongsimchae.domain.common.user.dto;

import lombok.Data;
import techit.gongsimchae.global.valid.JoinUnique;
import techit.gongsimchae.global.valid.NicknameUnique;

@Data
public class UserJoinReqDtoWeb {
    private String name;
    @JoinUnique
    private String email;

    private String password;
    private String passwordConfirm;
    @JoinUnique
    private String loginId;
    @NicknameUnique
    private String nickname;
    @JoinUnique
    private String phoneNumber;

    private String AuthCode;

}