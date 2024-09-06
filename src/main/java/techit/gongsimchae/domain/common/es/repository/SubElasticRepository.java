package techit.gongsimchae.domain.common.es.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.es.entity.SubdivisionDocument;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionRespDto;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubElasticRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    @Transactional
    public void createSubDocument(Subdivision subdivision, String url) {
        elasticsearchOperations.save(new SubdivisionDocument(subdivision, url));
    }

    public Page<SubdivisionRespDto> searchSubByTitle(String content) {
        PageRequest pageRequest = PageRequest.of(0, 10);

        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> b
                        .filter(f -> f
                                .term(t -> t
                                        .field("delete_status")
                                        .value(false)
                                )
                        )
                        .should(s -> s.match(w -> w
                                .field("title")
                                .query(content)
                                .analyzer("nori")
                        ))
                        .should(s -> s.match(w -> w
                                .field("content")
                                .query(content)
                                .analyzer("nori")
                        ))
                        .should(s -> s.match(w -> w
                                .field("address")
                                .query(content)
                                .analyzer("nori")
                        ))
                        .minimumShouldMatch("1")
                ))
                .withPageable(pageRequest)
                .build();


        SearchHits<SubdivisionDocument> searchHits = elasticsearchOperations.search(query,
                SubdivisionDocument.class);


        List<SubdivisionRespDto> resulsts = searchHits.getSearchHits().stream()
                .map(i -> new SubdivisionRespDto(i.getContent()))
                .toList();
        return new PageImpl<>(resulsts, pageRequest, searchHits.getTotalHits());
    }

    public void updateSubdivision(Subdivision subdivision, String url) {;
        Query query = new CriteriaQuery(Criteria.where("subdivision_id").is(subdivision.getId()));

        SubdivisionDocument subdivisionDocument = elasticsearchOperations.searchOne(query, SubdivisionDocument.class).getContent();
        subdivisionDocument.changeInfo(subdivision,url);

        Document updateDocument = elasticsearchOperations.getElasticsearchConverter().mapObject(subdivisionDocument);

        elasticsearchOperations.update(UpdateQuery.builder(subdivisionDocument.getId().toString())
                .withDocument(updateDocument)
                .withDocAsUpsert(true).build(), IndexCoordinates.of("subdivision_document"));


    }

    public void updateSubdivision(Subdivision subdivision) {;
        Query query = new CriteriaQuery(Criteria.where("subdivisionId").is(subdivision.getId()));

        SubdivisionDocument subdivisionDocument = elasticsearchOperations.searchOne(query, SubdivisionDocument.class).getContent();
        subdivisionDocument.changeInfo(subdivision);

        Document updateDocument = elasticsearchOperations.getElasticsearchConverter().mapObject(subdivisionDocument);

        elasticsearchOperations.update(UpdateQuery.builder(subdivisionDocument.getId().toString())
                .withDocument(updateDocument)
                .withDocAsUpsert(true).build(), IndexCoordinates.of("subdivision_document"));


    }
}
