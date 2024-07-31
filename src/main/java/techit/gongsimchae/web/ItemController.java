package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techit.gongsimchae.domain.admin.item.dto.ItemDto;
import techit.gongsimchae.domain.admin.item.entity.Item;
import techit.gongsimchae.domain.admin.item.service.ItemService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/item")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public String listItems(Model model) {
        model.addAttribute("items", itemService.getAllItems());
        return "admin/item/item";
    }

    @GetMapping("/create")
    public String showItemForm(Model model) {
        model.addAttribute("item", new ItemDto());
        model.addAttribute("isNew", true);
        return "admin/item/itemForm";
    }

    @PostMapping("/create")
    public String createItem(@ModelAttribute ItemDto itemDto) {
        Item item = convertToEntity(itemDto);
        itemService.save(item);
        return "redirect:/admin/item";
    }


    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Item item = itemService.getItemById(id);
        ItemDto itemDto = convertToDto(item);
        model.addAttribute("item", itemDto);
        model.addAttribute("isNew", false);
        return "admin/item/itemForm";
    }

    @PostMapping("/update")
    public String updateItem(@ModelAttribute ItemDto itemDto) {
        Item existingItem = itemService.getItemById(itemDto.getId());
        if (existingItem != null) {
            BeanUtils.copyProperties(itemDto, existingItem, "id", "createDate");
            if (itemDto.getGroupBuyingLimitTime() != null) {
                existingItem.setGroupBuyingLimitTime(itemDto.getGroupBuyingLimitTime());
            }
            itemService.save(existingItem);
        }
        return "redirect:/admin/item";
    }

    @PostMapping("/delete")
    public String deleteItem(@RequestParam Long id) {
        itemService.deleteItem(id);
        return "redirect:/admin/item";
    }

    private Item convertToEntity(ItemDto dto) {
        Item item = new Item();
        BeanUtils.copyProperties(dto, item);
        if (dto.getGroupBuyingLimitTime() != null) {
            item.setGroupBuyingLimitTime(dto.getGroupBuyingLimitTime());
        }
        return item;
    }

    private ItemDto convertToDto(Item item) {
        ItemDto dto = new ItemDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}