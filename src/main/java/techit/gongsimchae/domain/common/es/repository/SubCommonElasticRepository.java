package techit.gongsimchae.domain.common.es.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import techit.gongsimchae.domain.common.es.entity.SubdivisionDocument;

import java.util.Optional;

public interface SubCommonElasticRepository extends ElasticsearchRepository<SubdivisionDocument, String> {

    Page<SubdivisionDocument> findByTitle(String title, Pageable pageable);

    Optional<SubdivisionDocument> findById(String id);

    Optional<SubdivisionDocument> findBySubdivisionId(Long id);
}
