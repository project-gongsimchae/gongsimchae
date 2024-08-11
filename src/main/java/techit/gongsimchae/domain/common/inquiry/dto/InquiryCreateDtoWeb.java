package techit.gongsimchae.domain.common.inquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.common.inquiry.entity.InquiryType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryCreateDtoWeb {

    private String title;
    private String content;
    private InquiryType inquiryType;
    private String UID;
    private String email;
}
