package techit.gongsimchae.domain.common.inquiry.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class InquiryAdminReplyReqDtoWeb {

    private String title;
    @NotEmpty
    private String answer;
}
