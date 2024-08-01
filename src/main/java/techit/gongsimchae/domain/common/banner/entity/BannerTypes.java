package techit.gongsimchae.domain.common.banner.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BannerTypes {
    EVENT("공구 이벤트"), PORTION("소분 배너");
    String description;

}
