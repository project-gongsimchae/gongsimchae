package techit.gongsimchae.domain.admin.item.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemUpdateDto {
    private String name;
    private String intro;
    private Integer originalPrice;
    private Integer discountRate;
    private Integer pointAccumulationRate;
    private Integer groupBuyingQuantity;
    private LocalDateTime groupBuyingLimitTime;
    private String categoryName;


}
