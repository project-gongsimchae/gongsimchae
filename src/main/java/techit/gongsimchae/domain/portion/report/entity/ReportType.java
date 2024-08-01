package techit.gongsimchae.domain.portion.report.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReportType {
    ABUSE("욕설"), OTHER("기타");

    String description;
}