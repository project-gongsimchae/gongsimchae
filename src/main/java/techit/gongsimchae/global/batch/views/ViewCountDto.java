package techit.gongsimchae.global.batch.views;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ViewCountDto {
    private String subdivisionId;
    private long viewCount;
}
