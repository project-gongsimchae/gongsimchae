package techit.gongsimchae.domain.portion.chatroomuser.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomUserEndEvent {
    private Long subdivisionId;
    private String title;
}
