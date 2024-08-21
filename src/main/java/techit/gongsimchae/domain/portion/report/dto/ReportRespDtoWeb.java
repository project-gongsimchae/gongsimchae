package techit.gongsimchae.domain.portion.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.portion.report.entity.ReportType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRespDtoWeb {

    private Long id;

    private String title;
    private String content;
    private ReportType reportType;

}
