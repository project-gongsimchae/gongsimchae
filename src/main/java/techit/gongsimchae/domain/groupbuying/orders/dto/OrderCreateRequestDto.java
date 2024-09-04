package techit.gongsimchae.domain.groupbuying.orders.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderCreateRequestDto {
    private List<TempOrderItemDto> tempOrderItems;
}
