package techit.gongsimchae.domain.groupbuying.item.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import techit.gongsimchae.domain.groupbuying.itemoption.dto.ItemOptionCreateDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemCreateDto {
    private String name;
    private String intro;
    private Integer originalPrice;
    private Integer discountRate;
    private Integer pointAccumulationRate;
    private Integer groupBuyingQuantity;
    private LocalDateTime groupBuyingLimitTime;
    private String categoryName;
    private List<MultipartFile> images;

    private List<ItemOptionCreateDto> options = new ArrayList<>();

}
