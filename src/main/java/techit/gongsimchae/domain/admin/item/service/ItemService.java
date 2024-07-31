package techit.gongsimchae.domain.admin.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.admin.item.entity.Item;
import techit.gongsimchae.domain.admin.item.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public void save(Item item) {
        itemRepository.save(item);
    }

    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    /**
     * 최신등록 8개 아이템
     * 참여가 많은 8개 아이템을 리스트형태로 반환하는 메서드입니다. **/
    public List<Item> getRecentItems(){
        return itemRepository.findTop8ByOrderByCreateDateDesc();
    }

    public List<Item> getPopularItems(){
        return itemRepository.findTop8ByOrderByGroupBuyingQuantityDesc();
    }
}
