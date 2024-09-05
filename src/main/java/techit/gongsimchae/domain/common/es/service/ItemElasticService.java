package techit.gongsimchae.domain.common.es.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.common.es.repository.ItemElasticRepository;
import techit.gongsimchae.domain.common.participate.repository.ParticipateRepository;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemRespDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemSearchForm;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemElasticService {

    private final ItemElasticRepository itemElasticRepository;
    private final ParticipateRepository participateRepository;

    public List<ItemRespDtoWeb> getRelatedSearchTerms(ItemSearchForm itemSearchForm) {
        List<ItemRespDtoWeb> results = itemElasticRepository.searchByItems(itemSearchForm);
        setParticipateCount(results);
        return results;
    }

    private void setParticipateCount(List<ItemRespDtoWeb> itemRespDtoWebs) {
        for (ItemRespDtoWeb itemRespDtoWeb : itemRespDtoWebs) {
            Long count = participateRepository.countByItem(itemRespDtoWeb.getId());
            itemRespDtoWeb.setParticipateCount(count);
        }
    }
}
