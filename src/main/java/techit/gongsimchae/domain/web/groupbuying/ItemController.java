package techit.gongsimchae.domain.web.groupbuying;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techit.gongsimchae.domain.common.es.service.ItemElasticService;
import techit.gongsimchae.domain.common.imagefile.entity.ItemImageFileStatus;
import techit.gongsimchae.domain.common.participate.service.ParticipateService;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.category.service.CategoryService;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;
import techit.gongsimchae.domain.groupbuying.event.service.EventService;
import techit.gongsimchae.domain.groupbuying.eventcategory.service.EventCategoryService;
import techit.gongsimchae.domain.groupbuying.item.dto.*;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.item.entity.ItemStatus;
import techit.gongsimchae.domain.groupbuying.item.service.ItemService;
import techit.gongsimchae.domain.groupbuying.itemoption.dto.ItemOptionDto;
import techit.gongsimchae.domain.groupbuying.itemoption.service.ItemOptionService;
import techit.gongsimchae.domain.groupbuying.orderitem.service.OrderItemService;
import techit.gongsimchae.domain.groupbuying.reviews.service.ReviewService;
import techit.gongsimchae.global.dto.PrincipalDetails;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping
public class ItemController {
    private final ItemService itemService;
    private final CategoryService categoryService;
    private final ItemOptionService itemOptionService;
    private final EventCategoryService eventCategoryService;
    private final EventService eventService;
    private final OrderItemService orderItemService;
    private final ReviewService reviewService;
    private final ItemElasticService itemElasticService;
    private final ParticipateService participateService;

    /**
     * 검색
     */
    @GetMapping("/search")
    public String search(ItemSearchForm itemSearchForm,  Model model) {
        List<ItemRespDtoWeb> items = itemElasticService.getRelatedSearchTerms(itemSearchForm);
        model.addAttribute("keyword", itemSearchForm.getKeyword());
        model.addAttribute("items", items);
        return "groupbuying/search";
    }

    @GetMapping("/admin/item")
    public String listItems(@ModelAttribute ItemAdminSearchForm form, @PageableDefault(size = 10) Pageable pageable,
                            Model model) {
        Page<ItemRespDtoWeb> items = itemService.getItemsWithCriteria(form, pageable);
        List<Category> categories = categoryService.getAllRemainingCategories();
        model.addAttribute("items", items);
        model.addAttribute("categories", categories);
        return "admin/item/item";
    }

    @GetMapping("/admin/item/create")
    public String showItemForm(Model model) {
        model.addAttribute("item", new ItemCreateDto());
        model.addAttribute("categories", categoryService.getAllRemainingCategories());
        model.addAttribute("isNew", true);
        return "admin/item/createItemForm";
    }

    @PostMapping("/admin/item/create")
    public String createItem(@ModelAttribute ItemCreateDto itemCreateDto,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {

        itemService.createItem(itemCreateDto, principalDetails.getAccountDto().getId());
        return "redirect:/admin/item";
    }

    @GetMapping("/admin/item/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model){
        Item item = itemService.getItemById(id);
        if(item == null) {
            return "redirect:/admin/item";
        }
        model.addAttribute("item", item);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("isNew", false);
        model.addAttribute("images", item.getImageFiles().stream().filter(image -> image.getItemImageFileStatus().equals(ItemImageFileStatus.THUMBNAIL)).toList());
        model.addAttribute("detailImages", item.getImageFiles().stream().filter(image -> image.getItemImageFileStatus().equals(ItemImageFileStatus.DETAIL)).toList());

        return "admin/item/updateForm";
    }

    @PostMapping("/admin/item/update/{id}")
    public String updateItem(@PathVariable("id") Long id, @ModelAttribute ItemUpdateDto itemUpdateDto, Model model){
        Item item = itemService.updateItem(id, itemUpdateDto);

        model.addAttribute("item", item);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("isNew", false);
        model.addAttribute("images", item.getImageFiles().stream().filter(image -> image.getItemImageFileStatus().equals(ItemImageFileStatus.THUMBNAIL)).toList());
        model.addAttribute("detailImages", item.getImageFiles().stream().filter(image -> image.getItemImageFileStatus().equals(ItemImageFileStatus.DETAIL)).toList());

        return "admin/item/updateForm";
    }

    @PostMapping("/admin/item-status/update/{id}")
    public String updateItemStatus(@PathVariable("id") Long id){

        itemService.changeItemStatus(id, ItemStatus.공동구매_진행중);

        return "redirect:/admin/item";
    }

    @PostMapping("/admin/item/delete")
    public String deleteItem(@RequestParam Long id){
        itemService.deleteItem(id);
        return "redirect:/admin/item";
    }

    @PostMapping("/admin/item/restore")
    public String restoreItem(@RequestParam Long id){
        itemService.restoreItem(id);
        return "redirect:/admin/item";
    }

    /**
     * 유저가 보는 아이템
     */

    @GetMapping("/product/{id}")
    public String itemDetails(@PathVariable("id") Long id, Model model) {
        List<ItemOptionDto> item = itemOptionService.getItemOptionById(id);

        model.addAttribute("item",item);
        model.addAttribute("participateCount", participateService.getParticipateCount(item.get(0).getItemId()));
        model.addAttribute("reviewResDtoWebList", reviewService.findReviewListByItemId(id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser");
        model.addAttribute("isAuthenticated", isAuthenticated);

        return "groupbuying/itemDetails";
    }

    /**
     * 이하 카테고리
     */

    /**
     *  카테고리별 페이지 조회
     */
    @GetMapping("/category/{category_id}")
    public String getSelectedCategoryItems(@PathVariable Long category_id,
                                           @RequestParam(defaultValue = "1") Integer page,
                                           @RequestParam(defaultValue = "48") Integer per_page,
                                           @RequestParam(defaultValue = "1") Integer sorted_type, Model model){
        Category category = categoryService.getCategoryById(category_id);
        Page<ItemCardResDtoWeb> itemsPage = itemService.getItemsPageByCategory(category, page - 1,
                per_page, sorted_type);
        model.addAttribute("categoryId", category_id);
        model.addAttribute("categoryName", category.getName());
        model.addAttribute("itemsPage", itemsPage);
        return "category/category";
    }

    /**
     * 신상품 페이지 조회
     * 상품 등록일 기준 200건을 조회
     * 200건 내에서 sorted_type에 따라 정렬을 다르게 리턴
     */
    @GetMapping("/collection-groups/new-item")
    public String getNewItems(@RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "48") Integer per_page,
                              @RequestParam(defaultValue = "1") Integer sorted_type, Model model){
        Page<ItemCardResDtoWeb> newItemsPage = itemService.getTop200NewItemsPage(page - 1, per_page, sorted_type);
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
                               @RequestParam(defaultValue = "48") Integer per_page,
                               @RequestParam(defaultValue = "1") Integer sorted_type, Model model){
        Page<ItemCardResDtoWeb> bestItemsPage = itemService.getTop200BestItemsPage(page - 1, per_page, sorted_type);
        model.addAttribute("bestItemsPage", bestItemsPage);
        return "category/bestItem";
    }

    /**
     * 이벤트 클릭 시 페이지 조회
     * 상품 등록일 기준 200건을 조회
     * 200건 내에서 sorted_type에 따라 정렬을 다르게 리턴
     */
    @GetMapping("/collection-groups/event-item")
    public String getEventItems(@RequestParam Long eventId,
                                @RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "48") Integer per_page,
                                @RequestParam(defaultValue = "1") Integer sorted_type, Model model){
        Event event = eventService.getEvent(eventId);
        List<Category> categories = eventCategoryService.getCategoriesByEvent(event);
        Page<ItemCardResDtoWeb> eventItems = itemService.getCategoriesItems(categories, page - 1, per_page, sorted_type);
        model.addAttribute("eventId", eventId);
        model.addAttribute("eventName", event.getEventName());
        model.addAttribute("eventItems", eventItems);
        return "category/eventItem";
    }
}