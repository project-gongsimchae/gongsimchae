package techit.gongsimchae.domain.groupbuying.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemRespDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemSearchForm;

public interface ItemCustomRepository {
    Page<ItemRespDtoWeb> findItemsByKeyword(ItemSearchForm itemSearchForm, Pageable pageable);
}
