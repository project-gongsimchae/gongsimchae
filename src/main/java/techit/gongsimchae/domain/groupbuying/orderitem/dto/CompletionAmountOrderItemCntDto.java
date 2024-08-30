package techit.gongsimchae.domain.groupbuying.orderitem.dto;

import lombok.*;

/**
 * 결제까지 완료된 상태인 특정 아이템의 갯수를 담습니다.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompletionAmountOrderItemCntDto {
    private Integer completionAmountCnt;
}
