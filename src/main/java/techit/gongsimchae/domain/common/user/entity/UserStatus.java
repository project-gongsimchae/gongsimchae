package techit.gongsimchae.domain.common.user.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserStatus {
    NORMAL("일반"), PENALTY("제재");

    String description;
}
