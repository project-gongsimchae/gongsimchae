package techit.gongsimchae.domain.groupbuying.reviews.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SecretStatus {
    SECRET("비밀"), NORMAL("일반");
    String description;
}
