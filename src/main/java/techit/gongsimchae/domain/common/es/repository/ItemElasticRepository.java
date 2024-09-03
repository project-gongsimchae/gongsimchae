package techit.gongsimchae.domain.common.es.repository;

import co.elastic.clients.elasticsearch._types.SortOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;
import techit.gongsimchae.domain.common.es.entity.ItemDocument;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemRespDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemSearchForm;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemElasticRepository {
    private final ElasticsearchOperations elasticsearchOperations;

    public void createItemDocument(Item item, String url) {
        elasticsearchOperations.save(new ItemDocument(item, url));
    }

    public List<ItemRespDtoWeb> searchByItems(ItemSearchForm itemSearchForm) {
        PageRequest pageRequest = PageRequest.of(0, 20);
        String keyword = itemSearchForm.getKeyword();

        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> b
                                .should(s -> s.match(m -> m
                                                .field("name")
                                                .query(keyword)
                                                .analyzer("nori")
                                        ))
                                .should(s -> s
                                        .match(m -> m
                                                .field("intro")
                                                .query(keyword)
                                                .analyzer("nori")))
                                .mustNot(mn -> mn
                                        .term(t -> t
                                                .field("deleteStatus")
                                                .value(1)
                                        )
                                )
                        )
                )
                .withSort(s -> {
                    Integer sortType = itemSearchForm.getSortType();
                    if (sortType != null) {
                        switch (sortType) {
                            case 4:
                                return s.field(f -> f.field("originalPrice").order(SortOrder.Asc));
                            case 5:
                                return s.field(f -> f.field("originalPrice").order(SortOrder.Desc));
                            case 1:
                            default:
                                return s.field(f -> f.field("createDate").order(SortOrder.Desc));
                        }
                    }
                    return s.field(f -> f.field("createDate").order(SortOrder.Desc)); // 기본값: 생성일 내림차순
                })
                .withPageable(pageRequest)
                .build();

        SearchHits<ItemDocument> results = elasticsearchOperations.search(query, ItemDocument.class);
        return results.getSearchHits().stream()
                .map(hit -> new ItemRespDtoWeb(hit.getContent()))
                .toList();
    }
}

