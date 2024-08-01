package techit.gongsimchae.domain.groupbuying.reviews.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SecretStatus {
    SECRET("비밀"), NORMAL("일반");
    String description;
}
