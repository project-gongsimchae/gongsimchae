package techit.gongsimchae.global.event;

import lombok.Getter;

@Getter
public class TargetCountAchievedEvent {

    private Long itemId;

    public TargetCountAchievedEvent(Long itemId) {
        this.itemId = itemId;
    }

}
