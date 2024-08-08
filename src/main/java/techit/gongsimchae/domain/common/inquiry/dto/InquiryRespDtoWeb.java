package techit.gongsimchae.domain.common.inquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.common.inquiry.entity.Inquiry;
import techit.gongsimchae.domain.common.inquiry.entity.InquiryType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryRespDtoWeb {
    private Long id;

    private String title;
    private String content;
    private InquiryType inquiryType;
    private Integer isAnswered;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String UID;
    private String answer;


    // 문의를 작성한 사용자 정보
    private String name;
    private String nickname;
    private String email;

    public InquiryRespDtoWeb(Inquiry inquiry) {
        this.title = inquiry.getTitle();
        this.content = inquiry.getContent();
        this.inquiryType = inquiry.getInquiryType();
        this.isAnswered = inquiry.getIsAnswered();
        this.createDate = inquiry.getCreateDate();
        this.updateDate = inquiry.getUpdateDate();
        this.UID = inquiry.getUID();
        this.answer = inquiry.getAnswer();
    }
}
