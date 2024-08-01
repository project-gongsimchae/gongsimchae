package techit.gongsimchae.domain.common.participate.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PurchaseType {
    GROUP_BUY("공동구매"),SPLIT_BUY("소분");
    String Description;
}
