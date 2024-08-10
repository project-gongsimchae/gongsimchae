package techit.gongsimchae.domain.common.inquiry.dto;

import lombok.Data;
import techit.gongsimchae.domain.common.inquiry.entity.Inquiry;
import techit.gongsimchae.domain.common.inquiry.entity.InquiryType;

import java.time.LocalDateTime;

@Data
public class InquiryAdminReplyReqDtoWeb {
    private String answer;
}
