package techit.gongsimchae.global.event;

import lombok.Getter;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;

@Getter
public class TargetCountAchievedEvent {

    private Long itemId;

    public TargetCountAchievedEvent(Long itemId) {
        this.itemId = itemId;
    }

}
