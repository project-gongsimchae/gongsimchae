package techit.gongsimchae.domain.common.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.global.valid.JoinUnique;
import techit.gongsimchae.global.valid.NicknameUnique;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    private String address;
    private String zipcode;
    private String detailAddress;

}
