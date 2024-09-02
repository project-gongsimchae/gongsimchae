package techit.gongsimchae.domain.common.inquiry.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import techit.gongsimchae.domain.common.inquiry.entity.InquiryType;

@Data
public class InquiryUpdateReqDtoWeb {
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    @NotNull
    private InquiryType inquiryType;
    private String UID;
    private String email;
}
