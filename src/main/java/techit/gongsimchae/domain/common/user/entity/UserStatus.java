package techit.gongsimchae.domain.common.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus {
    NORMAL("일반"), PENALTY("제재");

    String description;
}
