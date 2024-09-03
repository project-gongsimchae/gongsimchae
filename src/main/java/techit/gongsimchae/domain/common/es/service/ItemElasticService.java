package techit.gongsimchae.domain.common.es.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.common.es.repository.ItemElasticRepository;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemRespDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemSearchForm;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemElasticService {

    private final ItemElasticRepository itemElasticRepository;

    public List<ItemRespDtoWeb> getRelatedSearchTerms(ItemSearchForm itemSearchForm) {
        return itemElasticRepository.searchByItems(itemSearchForm);
    }
}
