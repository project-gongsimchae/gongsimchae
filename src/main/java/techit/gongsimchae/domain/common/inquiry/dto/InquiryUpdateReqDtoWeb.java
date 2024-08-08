package techit.gongsimchae.domain.common.inquiry.dto;

import lombok.Data;
import techit.gongsimchae.domain.common.inquiry.entity.InquiryType;

@Data
public class InquiryUpdateReqDtoWeb {
    private String title;
    private String content;
    private InquiryType inquiryType;
    private String UID;
    private String email;
}
