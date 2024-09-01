package techit.gongsimchae.domain.common.es.dto;

import lombok.Data;
import techit.gongsimchae.domain.common.es.entity.SubdivisionDocument;

@Data
public class SubdivisionEsRespDto {

    private String id;

    private Long subdivisionId;

    private String title;

    private String content;

    private String address;

    private String subdivisionType;

    private Integer price;

    private Integer views;

    private String url;

    public SubdivisionEsRespDto(SubdivisionDocument subdivisionDocument) {
        this.id = subdivisionDocument.getId();
        this.subdivisionId = subdivisionDocument.getSubdivisionId();
        this.title = subdivisionDocument.getTitle();
        this.content = subdivisionDocument.getContent();
        this.address = subdivisionDocument.getAddress();
        this.subdivisionType = subdivisionDocument.getSubdivisionType();
        this.price = subdivisionDocument.getPrice();
        this.views = subdivisionDocument.getViews();
        this.url = subdivisionDocument.getUrl();
    }
}
