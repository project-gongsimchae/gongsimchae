package techit.gongsimchae.domain.admin.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.admin.item.dto.ItemCreateDto;
import techit.gongsimchae.domain.admin.item.dto.ItemUpdateDto;
import techit.gongsimchae.domain.admin.item.entity.Item;
import techit.gongsimchae.domain.admin.item.repository.ItemRepository;
import techit.gongsimchae.domain.category.entity.Category;
import techit.gongsimchae.domain.category.repository.CategoryRepository;

import java.util.List;

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


}
