package techit.gongsimchae.domain.groupbuying.item.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItemAdminSearchForm {
    private Integer itemIsDeleted;
    private Boolean remainingTime;
    private Integer itemIsClosed;
    private List<Long> categories;
}
