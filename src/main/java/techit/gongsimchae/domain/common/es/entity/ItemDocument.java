package techit.gongsimchae.domain.common.es.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;

import java.time.LocalDate;

@Document(indexName = "item_document")
@NoArgsConstructor
@AllArgsConstructor
@Setting(settingPath = "/static/elastic/item-settings.json")
@Mapping(mappingPath = "/static/elastic/item-mappings.json")
@Getter
public class ItemDocument {

    @Id
    @Field(name = "item_id", type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String name;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String intro;

    @Field(name = "original_price", type = FieldType.Integer)
    private Integer originalPrice;

    @Field(name = "discount_rate", type = FieldType.Integer)
    private Integer discountRate;

    @Field(name = "group_buying_quantity", type = FieldType.Integer)
    private Integer groupBuyingQuantity;

    @Field(name = "create_date", type = FieldType.Date, format = DateFormat.basic_date)
    private LocalDate createDate;

    @Field(name = "delete_status", type = FieldType.Integer)
    private Integer deleteStatus;
    @Field(type = FieldType.Keyword)
    private String uid;

    @Field(type = FieldType.Keyword,index = false, docValues = false)
    private String url;

    public ItemDocument(Item item, String url) {
        this.id = item.getId();
        this.name = item.getName();
        this.intro = item.getIntro();
        this.originalPrice = item.getOriginalPrice();
        this.discountRate = item.getDiscountRate();
        this.groupBuyingQuantity = item.getGroupBuyingQuantity();
        this.createDate = item.getCreateDate().toLocalDate();
        this.deleteStatus = item.getDeleteStatus();
        this.uid = item.getUID();
        this.url = url;
    }
}
