package techit.gongsimchae.domain.common.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserFindPwReqDtoWeb {
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String email;
}
