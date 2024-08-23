package techit.gongsimchae.domain.groupbuying.point.entity;

import lombok.Getter;

@Getter
public enum PointType {

    EARNED("적립"),USED("사용"),EXPiRED("만료"),REFUNDED("환불");
    String description;

    PointType(String description) {
        this.description = description;
    }
}
