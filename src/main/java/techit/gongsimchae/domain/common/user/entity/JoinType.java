package techit.gongsimchae.domain.common.user.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum JoinType {
    NORMAL("자체 회원가입"), OAUTH("OAUTH 회원가입");
    String description;
}