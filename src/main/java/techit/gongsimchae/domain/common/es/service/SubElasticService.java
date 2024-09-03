package techit.gongsimchae.domain.common.es.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.es.repository.SubElasticRepository;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionRespDto;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubElasticService {

    private final SubElasticRepository subElasticRepository;

    public Page<SubdivisionRespDto> getRelatedSearchTerms(String content) {

        return subElasticRepository.searchSubByTitle(content);
    }


}
