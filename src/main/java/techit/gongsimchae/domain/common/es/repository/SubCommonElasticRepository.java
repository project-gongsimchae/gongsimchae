package techit.gongsimchae.domain.common.es.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import techit.gongsimchae.domain.common.es.entity.SubdivisionDocument;

public interface SubCommonElasticRepository extends ElasticsearchRepository<SubdivisionDocument, Long> {
}
