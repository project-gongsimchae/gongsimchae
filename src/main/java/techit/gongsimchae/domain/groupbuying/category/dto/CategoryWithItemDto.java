package techit.gongsimchae.domain.groupbuying.category.dto;

import lombok.Data;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;

@Data
public class CategoryWithItemDto {
    private Long id;
    private String name;
    private boolean hasActiveItem;

    public CategoryWithItemDto(Category category, boolean hasActiveItem) {
        this.id = category.getId();
        this.name = category.getName();
        this.hasActiveItem = hasActiveItem;
    }
}
