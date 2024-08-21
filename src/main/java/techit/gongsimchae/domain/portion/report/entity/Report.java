package techit.gongsimchae.domain.portion.report.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.portion.report.dto.ReportCreateReqDtoWeb;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;


@Entity
@Getter
@NoArgsConstructor
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subdivision_id")
    private Subdivision subdivision;

    public Report(ReportCreateReqDtoWeb createReqDtoWeb, User user, Subdivision subdivision) {
        this.title = createReqDtoWeb.getTitle();
        this.content = createReqDtoWeb.getContent();
        this.reportType = createReqDtoWeb.getReportType();
        this.user = user;
        this.subdivision = subdivision;
    }
}
