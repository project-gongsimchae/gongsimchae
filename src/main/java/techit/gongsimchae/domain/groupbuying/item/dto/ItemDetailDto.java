package techit.gongsimchae.domain.groupbuying.item.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ItemDetailDto {
    private Long id;
    private String name;
    private String intro;
    private Integer originalPrice;
    private Integer discountRate;
    private Integer pointAccumulationRate;
    private Integer groupBuyingQuantity;
    private LocalDateTime groupBuyingLimitTime;
    private LocalDate createDate;
    private String UID;
    private Long categoryId;
}
