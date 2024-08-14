package techit.gongsimchae.domain.portion.subdivision.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubdivisionType {
    RECRUITING("모집 중"),
    RECRUITMENT_COMPLETED("모집 완료"),
    END("거래 완료");

    String description;

}
