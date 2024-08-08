package techit.gongsimchae.domain.common.inquiry.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.common.inquiry.dto.InquiryCreateDtoWeb;
import techit.gongsimchae.domain.common.inquiry.dto.InquiryUpdateReqDtoWeb;
import techit.gongsimchae.domain.common.user.entity.User;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String UID;
    @Enumerated(EnumType.STRING)
    private InquiryType inquiryType;

    // 0 미완료 1 완료
    private Integer isAnswered;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Inquiry(InquiryCreateDtoWeb dtoWeb, User user) {
        this.title = dtoWeb.getTitle();
        this.content = dtoWeb.getContent();
        this.inquiryType = dtoWeb.getInquiryType();
        this.user = user;
        this.isAnswered = 0;
        this.UID = UUID.randomUUID().toString();
    }

    public void changeInfo(InquiryUpdateReqDtoWeb inquiryUpdateReqDtoWeb) {
        this.title = inquiryUpdateReqDtoWeb.getTitle();
        this.content = inquiryUpdateReqDtoWeb.getContent();
        this.inquiryType = inquiryUpdateReqDtoWeb.getInquiryType();
    }
}
