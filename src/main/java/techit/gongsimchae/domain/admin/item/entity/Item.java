package techit.gongsimchae.domain.admin.item.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String intro;
    private Integer originalPrice;
    private Integer discountRate;
    private Integer pointAccumulationRate;
    private Integer groupBuyingQuantity;
    private LocalDateTime groupBuyingLimitTime;
    private LocalDate createDate;
    private Integer deleteStatus;
    private String UID;
}
