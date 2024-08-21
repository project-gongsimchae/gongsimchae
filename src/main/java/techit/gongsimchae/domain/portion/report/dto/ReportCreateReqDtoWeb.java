package techit.gongsimchae.domain.portion.report.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import techit.gongsimchae.domain.portion.report.entity.ReportType;

@Data
@AllArgsConstructor
public class ReportCreateReqDtoWeb {

    @NotEmpty
    private String title;
    private String content;
    private String post;

    private ReportType reportType;
}
