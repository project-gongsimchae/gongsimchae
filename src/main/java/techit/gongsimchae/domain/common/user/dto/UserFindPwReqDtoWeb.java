package techit.gongsimchae.domain.common.user.dto;

import lombok.Data;

@Data
public class UserFindPwReqDtoWeb {
    private String loginId;
    private String email;
}
