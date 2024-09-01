package techit.gongsimchae.domain.common.es.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.es.dto.SubdivisionEsRespDto;
import techit.gongsimchae.domain.common.es.repository.SubCommonElasticRepository;
import techit.gongsimchae.domain.common.es.repository.SubSearchRepository;
import techit.gongsimchae.domain.portion.subdivision.repository.SubdivisionRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubElasticService {

    private final SubCommonElasticRepository subCommonElasticRepository;
    private final SubSearchRepository subSearchRepository;
    private final SubdivisionRepository subdivisionRepository;

    public List<SubdivisionEsRespDto> getRelatedSearchTerms(String content) {

        return subSearchRepository.searchSubByTitle(content);
    }


}
