package techit.gongsimchae.domain.common.es.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Repository;
import techit.gongsimchae.domain.common.es.dto.SubdivisionEsRespDto;
import techit.gongsimchae.domain.common.es.entity.SubdivisionDocument;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SubSearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    public List<SubdivisionEsRespDto> searchSubByTitle(String content) {
        PageRequest pageRequest = PageRequest.of(0, 10);

        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> b
                        .should(s -> s.match(w -> w
                                .field("title")
                                .query(content)
                                .analyzer("nori")
                                .boost(2.0f)
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
                ))
                .withPageable(pageRequest)
                .build();


        SearchHits<SubdivisionDocument> searchHits = elasticsearchOperations.search(query,
                SubdivisionDocument.class);


        List<SubdivisionDocument> results = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();

        return results.stream()
                .map(SubdivisionEsRespDto::new)
                .collect(Collectors.toList());
    }

    public void updateSubdivision(Subdivision subdivision, String url) {;
        Query query = new CriteriaQuery(Criteria.where("subdivisionId").is(subdivision.getId()));

        SubdivisionDocument subdivisionDocument = elasticsearchOperations.searchOne(query, SubdivisionDocument.class).getContent();
        subdivisionDocument.changeInfo(subdivision,url);

        Document updateDocument = elasticsearchOperations.getElasticsearchConverter().mapObject(subdivisionDocument);

        elasticsearchOperations.update(UpdateQuery.builder(subdivisionDocument.getId())
                .withDocument(updateDocument)
                .withDocAsUpsert(true).build(), IndexCoordinates.of("subdivision_document"));

    }
}
