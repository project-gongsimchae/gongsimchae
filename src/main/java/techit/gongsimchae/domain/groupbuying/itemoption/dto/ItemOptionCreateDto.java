package techit.gongsimchae.domain.groupbuying.itemoption.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ItemOptionCreateDto {
    private String content;
    private Integer price;

    public ItemOptionCreateDto() {}

    @Builder
    public ItemOptionCreateDto(String content, Integer price) {
        this.content = content;
        this.price = price;
    }

}
