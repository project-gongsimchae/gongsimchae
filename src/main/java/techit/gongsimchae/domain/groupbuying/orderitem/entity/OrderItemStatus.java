package techit.gongsimchae.domain.groupbuying.orderitem.entity;

import lombok.Getter;

@Getter
public enum OrderItemStatus {
    주문완료, 주문취소, 결제완료, 결제취소, 공동구매성공, 공동구매실패
}
