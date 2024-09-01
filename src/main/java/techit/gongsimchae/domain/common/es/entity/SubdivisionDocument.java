package techit.gongsimchae.domain.common.es.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;

@Document(indexName = "subdivision_document")
@NoArgsConstructor
@AllArgsConstructor
@Setting(settingPath = "/static/elastic/elastic-settings.json")
@Mapping(mappingPath = "/static/elastic/product-mappings.json")
@Getter
public class SubdivisionDocument {
    @Id
    private String id;

    @Field(name = "subdivision_id", type = FieldType.Long)
    private Long subdivisionId;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String title;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String content;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String address;

    @Field(name = "subdivision_type", type = FieldType.Keyword, index = false, docValues = false)
    private String subdivisionType;

    @Field(type = FieldType.Integer, index = false, docValues = false)
    private Integer price;
    @Field(type = FieldType.Integer, index = false, docValues = false)
    private Integer views;
    @Field(type = FieldType.Keyword, index = false, docValues = false)
    private String url;

    public SubdivisionDocument(Subdivision subdivision, String url) {
        this.subdivisionId = subdivision.getId();
        this.title = subdivision.getTitle();
        this.content = subdivision.getContent();
        this.address = subdivision.getAddress();
        this.subdivisionType = subdivision.getSubdivisionType().name();
        this.price = subdivision.getPrice();
        this.views = subdivision.getViews();
        this.url = url;
    }

    public void changeInfo(Subdivision subdivision, String url) {
        this.title = subdivision.getTitle();
        this.content = subdivision.getContent();
        this.address = subdivision.getAddress();
        this.price = subdivision.getPrice();
        this.views = subdivision.getViews();
        this.url = url;
        this.subdivisionType = subdivision.getSubdivisionType().name();
    }
}
