package techit.gongsimchae.domain.groupbuying.itemoption.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;

@Entity
@Getter
@NoArgsConstructor
public class ItemOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String options;
    private String content;
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public ItemOption(Item item, String content, Integer price){
        this.item = item;
        this.content = content;
        this.price = price;
    }

    public void updateOption(String content, Integer price) {
        this.content = content;
        this.price = price;
    }
}
