package techit.gongsimchae.domain.groupbuying.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemCardResDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemRespDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemSearchForm;

public interface ItemCustomRepository {
    Page<ItemCardResDtoWeb> findRecentItems(Pageable pageable);
}
