package techit.gongsimchae.domain.groupbuying.item.dto;

import lombok.Data;

@Data
public class ItemSearchForm {
    private String keyword;
    private Integer page;
    private Integer perPage;
    private Integer sortType;
}
