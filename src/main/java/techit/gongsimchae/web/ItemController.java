package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemCreateDto;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemRespDtoWeb;
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
     * 유저가 보는 아이템
     */

    @GetMapping("/product/{id}")
    public String itemDetails(@PathVariable("id") String id, Model model) {
        ItemRespDtoWeb item = itemService.getItem(id);
        model.addAttribute("item",item);
        return "groupbuying/itemDetails";
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

    /**
     * 신상품 페이지 조회
     * 상품 등록일 기준 200건을 조회
     * 200건 내에서 sorted_type에 따라 정렬을 다르게 리턴
     */
    @GetMapping("/collection-groups/new-item")
    public String getNewItems(@RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "96") Integer per_page,
                              @RequestParam(defaultValue = "1") Integer sorted_type, Model model){
        Page<Item> newItemsPage = itemService.getTop200NewItemsPage(page - 1, per_page, sorted_type);
        model.addAttribute("newItemsPage", newItemsPage);
        return "category/newItem";
    }

    /**
     * 베스트 페이지 조회
     * 누적 판매량 기준 200건을 조회
     * 200건 내에서 sorted_type에 따라 정렬을 다르게 리턴
     */

    @GetMapping("/collection-groups/best-item")
    public String getBestItems(@RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "96") Integer per_page,
                              @RequestParam(defaultValue = "1") Integer sorted_type, Model model){
        Page<Item> bestItemsPage = itemService.getTop200BestItemsPage(page - 1, per_page, sorted_type);
        model.addAttribute("bestItemsPage", bestItemsPage);
        return "category/bestItem";
    }
}