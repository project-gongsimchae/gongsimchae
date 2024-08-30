package techit.gongsimchae.domain.groupbuying.item.entity;

import lombok.Getter;

/**
 * 공동 구매 현황에 대해 나타내는 상태 enum 값 입니다.
 */
@Getter
public enum ItemStatus {
    공동구매_준비중, 공동구매_진행중, 공동구매_마감
}
