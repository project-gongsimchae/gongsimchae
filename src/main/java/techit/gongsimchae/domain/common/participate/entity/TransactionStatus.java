package techit.gongsimchae.domain.common.participate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionStatus {
    PENDING("거래 전"), INPROGESS("거래 중"), COMPLETED("거래 완료");
    String description;
}
