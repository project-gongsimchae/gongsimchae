package techit.gongsimchae.domain.groupbuying.itemoption.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.item.service.ItemService;
import techit.gongsimchae.domain.groupbuying.itemoption.dto.ItemOptionDto;
import techit.gongsimchae.domain.groupbuying.itemoption.entity.ItemOption;
import techit.gongsimchae.domain.groupbuying.itemoption.repository.ItemOptionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemOptionService {
    private final ItemOptionRepository itemOptionRepository;
    private final ItemService itemService;
    @Transactional(readOnly = true)
    public List<ItemOptionDto> getItemOptionById(Long itemId){
        Item item = itemService.getItemById(itemId);
        List<ItemOption> itemOptions = itemOptionRepository.findAllByItemId(itemId);


        return itemOptions.stream()
                .map(option -> convertToItemOptionDto(item, option))
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemOptionDto convertToItemOptionDto(Item item, ItemOption itemOption){
        int originalPrice = item.getOriginalPrice();
        int optionPrice = itemOption.getPrice();
        int totalPrice = originalPrice + optionPrice;
        int discountPrice = (int) (totalPrice * (item.getDiscountRate() / 100.0));
        discountPrice = totalPrice - discountPrice;


        return ItemOptionDto.builder()
                .itemId(item.getId())
                .itemOptionId(itemOption.getId())
                .content(itemOption.getContent())
                .optionsInfo(itemOption.getOptions())
                .itemName(item.getName())
                .optionPrice(optionPrice)
                .totalPrice(totalPrice)
                .originalPrice(originalPrice)
                .discountRate(item.getDiscountRate())
                .discountPrice(discountPrice)
                .quantity(item.getGroupBuyingQuantity())
                .itemUID(item.getUID())
                .build();
    }
}
