package techit.gongsimchae.domain.groupbuying.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemSearchForm {
    private String keyword;
    private Integer sortType;
}
