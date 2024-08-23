package techit.gongsimchae.domain.portion.subdivision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubdivisionReportRespDto {

    private Long subdivisionId;
    private String title;
    private Long reportCount;
    private String email;
    private String nickname;
}
