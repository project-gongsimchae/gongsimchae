package techit.gongsimchae.domain.groupbuying.item.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import techit.gongsimchae.domain.groupbuying.itemoption.dto.ItemOptionUpdateDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<MultipartFile> images;
    private List<Long> deleteImages;
    private List<ItemOptionUpdateDto> options = new ArrayList<>();

}
