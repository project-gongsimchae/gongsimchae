package techit.gongsimchae.domain.common.participate.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransactionStatus {
    PENDING("거래 전"), INPROGESS("거래 중"), COMPLETED("거래 완료");
    String description;
}
