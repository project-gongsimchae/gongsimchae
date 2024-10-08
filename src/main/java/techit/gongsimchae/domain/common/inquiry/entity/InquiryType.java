package techit.gongsimchae.domain.common.inquiry.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InquiryType {
    CANCEL_EXCHANGE_RETURN("취소/교환/환불"),
    USER_EVENT_COUPON("회원/이벤트/쿠폰"),
    PRODUCT("상품"),
    PORTION("소분"),
    ETC("기타");
    String description;

}
