package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemCreateDto;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemUpdateDto;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.item.service.ItemService;
import techit.gongsimchae.domain.groupbuying.category.service.CategoryService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping
public class ItemController {
    private final ItemService itemService;
    private final CategoryService categoryService;

    @GetMapping("/admin/item")
    public String listItems(Model model) {
        model.addAttribute("items", itemService.getAllItems());
        return "admin/item/item";
    }

    @GetMapping("/admin/item/create")
    public String showItemForm(Model model) {
        model.addAttribute("item", new ItemCreateDto());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("isNew", true);
        return "admin/item/createForm";
    }

    @PostMapping("/admin/item/create")
    public String createItem(@ModelAttribute ItemCreateDto itemCreateDto) {
        itemService.createItem(itemCreateDto);
        return "redirect:/admin/item";
    }

    @GetMapping("/admin/item/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model){
        Item item = itemService.getItemById(id);
        if(item == null) {
            return "redirect:/admin/item";
        }
        model.addAttribute("item", item);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("isNew", false);
        return "admin/item/updateForm";
    }

    @PostMapping("/admin/item/update/{id}")
    public String updateItem(@PathVariable Long id, @ModelAttribute ItemUpdateDto itemUpdateDto){
        itemService.updateItem(id, itemUpdateDto);
        return "redirect:/admin/item";
    }

    @PostMapping("/admin/item/delete")
    public String deleteItem(@RequestParam Long id){
        itemService.deleteItem(id);
        return "redirect:/admin/item";
    }

    /**
     * 이하 카테고리
     */

    /**
     *  카테고리별 결과 가져오기
     */
    @GetMapping("/category/{category_id}")
    public String getSelectedCategoryItems(@PathVariable Long category_id,
                                           @RequestParam Integer page,
                                           @RequestParam Integer per_page,
                                           @RequestParam Integer sorted_type, Model model){
        Category category = categoryService.getCategoryById(category_id);
        Page<Item> itemsPage = itemService.getItemsPageByCategory(category, page - 1, per_page, sorted_type);
        model.addAttribute("categoryId", category_id);
        model.addAttribute("categoryName", category.getName());
        model.addAttribute("itemsPage", itemsPage);
        return "/category/category";
    }
}