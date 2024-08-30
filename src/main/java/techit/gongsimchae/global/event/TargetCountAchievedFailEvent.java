package techit.gongsimchae.global.event;

import lombok.Getter;

@Getter
public class TargetCountAchievedFailEvent {

    private Long itemId;

    public TargetCountAchievedFailEvent(Long itemId) {
        this.itemId = itemId;
    }
}
