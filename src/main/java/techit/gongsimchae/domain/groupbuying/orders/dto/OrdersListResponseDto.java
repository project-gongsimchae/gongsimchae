package techit.gongsimchae.domain.groupbuying.orders.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersListResponseDto {

    private Long id;
    private OrderStatus orderStatus;
    private String impUid;
    private String merchantUid;
    private Integer totalPrice;
    private Long userId;
    private String userName;
    private LocalDateTime createDate;
}
