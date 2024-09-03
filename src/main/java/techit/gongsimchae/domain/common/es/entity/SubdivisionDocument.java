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
    @Field(name = "subdivision_id", type = FieldType.Long)
    private Long id;

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
    @Field(type = FieldType.Keyword)
    private String uid;

    @Field(name = "delete_status", type = FieldType.Boolean, index = false)
    private Boolean deleteStatus;

    public SubdivisionDocument(Subdivision subdivision, String url) {
        this.id = subdivision.getId();
        this.title = subdivision.getTitle();
        this.content = subdivision.getContent();
        this.address = subdivision.getAddress();
        this.subdivisionType = subdivision.getSubdivisionType().name();
        this.price = subdivision.getPrice();
        this.views = subdivision.getViews();
        this.url = url;
        this.uid = subdivision.getUID();
        this.deleteStatus = subdivision.getDeleteStatus();
    }

    public void changeInfo(Subdivision subdivision, String url) {
        this.title = subdivision.getTitle();
        this.content = subdivision.getContent();
        this.address = subdivision.getAddress();
        this.price = subdivision.getPrice();
        this.views = subdivision.getViews();
        this.url = url;
        this.subdivisionType = subdivision.getSubdivisionType().name();
        this.deleteStatus = subdivision.getDeleteStatus();
    }

    public void changeInfo(Subdivision subdivision) {
        this.title = subdivision.getTitle();
        this.content = subdivision.getContent();
        this.address = subdivision.getAddress();
        this.price = subdivision.getPrice();
        this.views = subdivision.getViews();
        this.subdivisionType = subdivision.getSubdivisionType().name();
        this.deleteStatus = subdivision.getDeleteStatus();
    }
}
