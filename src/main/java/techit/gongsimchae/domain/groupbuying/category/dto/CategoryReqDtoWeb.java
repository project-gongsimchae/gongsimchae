package techit.gongsimchae.domain.groupbuying.category.dto;

import lombok.Data;
import techit.gongsimchae.global.valid.CategoryNameUnique;

@Data
public class CategoryReqDtoWeb {

    @CategoryNameUnique
    private String categoryName;
    private Long categoryId;

}
