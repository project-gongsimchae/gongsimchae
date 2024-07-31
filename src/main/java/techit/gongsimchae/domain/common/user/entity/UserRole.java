package techit.gongsimchae.domain.common.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
    ROLE_USER("유저"), ROLE_MANAGER("매니저"), ROLE_ADMIN("관리자");

    String description;
}
