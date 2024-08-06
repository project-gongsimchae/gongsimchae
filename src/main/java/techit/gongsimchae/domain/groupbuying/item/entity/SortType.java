package techit.gongsimchae.domain.groupbuying.item.entity;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.global.exception.CustomWebException;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum SortType {
    신상품순(1),
    판매량순(2),
    리뷰많은순(3),
    낮은가격순(4),
    높은가격순(5);

    private Integer typeNumber;

    public static SortType getInstanceByTypeNumber(Integer typeNumber) {
        SortType sortTypeByTypeNumber = Arrays.stream(values()).filter(value -> value.typeNumber.equals(typeNumber))
                .findFirst().orElseThrow(() -> new CustomWebException("존재하지 않는 정렬기준입니다."));
        return sortTypeByTypeNumber;
    }
}
