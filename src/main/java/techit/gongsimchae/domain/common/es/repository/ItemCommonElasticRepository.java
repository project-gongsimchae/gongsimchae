package techit.gongsimchae.domain.common.es.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import techit.gongsimchae.domain.common.es.entity.ItemDocument;

public interface ItemCommonElasticRepository extends ElasticsearchRepository<ItemDocument, Long> {
}
