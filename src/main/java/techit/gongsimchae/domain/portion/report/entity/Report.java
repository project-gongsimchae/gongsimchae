package techit.gongsimchae.domain.portion.report.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.common.user.entity.User;


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

    public Report(String title, String content, ReportType reportType) {
        this.title = title;
        this.content = content;
        this.reportType = reportType;

    }
}
