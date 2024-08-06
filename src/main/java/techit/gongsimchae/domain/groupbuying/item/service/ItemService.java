package techit.gongsimchae.domain.groupbuying.item.service;

import static techit.gongsimchae.domain.groupbuying.item.entity.SortType.*;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.category.repository.CategoryRepository;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemCreateDto;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemRespDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemUpdateDto;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.item.entity.SortType;
import techit.gongsimchae.domain.groupbuying.item.repository.ItemRepository;
import techit.gongsimchae.global.exception.CustomWebException;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    public void save(Item item) {
        itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }


    @Transactional
    public void createItem(ItemCreateDto itemCreateDto) {
        Category category = categoryRepository.findByName(itemCreateDto.getCategoryName())
                .orElseThrow(() -> {
                            throw new IllegalArgumentException("Category not found");
                        }
                );
        Item item = new Item(itemCreateDto, category);
        itemRepository.save(item);

    }

    @Transactional
    public void updateItem(Long id, ItemUpdateDto itemUpdateDto) {
        Item item = itemRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException("Item not found");
        });

        Category category = categoryRepository.findByName(itemUpdateDto.getCategoryName())
                .orElseThrow(() -> {
                            throw new IllegalArgumentException("Category not found");
                        }
                );

        item.UpdateDto(itemUpdateDto, category);
        itemRepository.save(item);
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

    public List<Item> getItemsByCategory(Category category) {
        return itemRepository.findAllByCategory(category);
    }

    public Page<Item> getItemsPageByCategory(Category category, Integer page, Integer per_page,
                                                   Integer sorted_type){
        SortType sortType = getInstanceByTypeNumber(sorted_type);
        Pageable pageable;
        if (sortType.equals(신상품순)){
            pageable = PageRequest.of(page, per_page, Sort.by(Direction.DESC,"createDate"));
        } else if (sortType.equals(판매량순)) {
            pageable = PageRequest.of(page, per_page, Sort.by(Direction.DESC, "cumulativeSalesVolume"));
        } else if (sortType.equals(리뷰많은순)){
            pageable = PageRequest.of(page, per_page, Sort.by(Direction.DESC, "reviewCount").descending());
        } else if (sortType.equals(낮은가격순)){
            pageable = PageRequest.of(page, per_page, Sort.by(Direction.ASC, "originalPrice").ascending());
        } else if (sortType.equals(높은가격순)) {
            pageable = PageRequest.of(page, per_page, Sort.by(Direction.DESC, "originalPrice").descending());
        } else {
            throw new CustomWebException("존재하지 않는 정렬기준입니다.");
        }
        return itemRepository.findAllByCategory(category, pageable);
    }

    public ItemRespDtoWeb getItem(String id) {
        Item item = itemRepository.findByUID(id).orElseThrow(() -> new CustomWebException("not found item"));
        return new ItemRespDtoWeb(item);
    }
}
