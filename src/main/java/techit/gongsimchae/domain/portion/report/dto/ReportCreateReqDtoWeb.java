package techit.gongsimchae.domain.portion.report.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import techit.gongsimchae.domain.portion.report.entity.ReportType;

@Data
@AllArgsConstructor
public class ReportCreateReqDtoWeb {

    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    private String uid;
    @NotNull
    private ReportType reportType;
}
