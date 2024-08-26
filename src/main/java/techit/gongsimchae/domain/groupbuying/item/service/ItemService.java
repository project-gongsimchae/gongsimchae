package techit.gongsimchae.domain.groupbuying.item.service;

import static techit.gongsimchae.domain.groupbuying.item.entity.SortType.*;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.imagefile.repository.ImageFileRepository;
import techit.gongsimchae.domain.common.imagefile.service.ImageS3Service;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.category.repository.CategoryRepository;
import techit.gongsimchae.domain.groupbuying.event.repository.EventRepository;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemCardResDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemCreateDto;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemRespDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemSearchForm;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemUpdateDto;
import techit.gongsimchae.domain.groupbuying.item.dto.ReviewAbleItemResDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.dto.ReviewItemResDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.dto.ReviewedItemResDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.item.entity.SortType;
import techit.gongsimchae.domain.groupbuying.item.repository.ItemRepository;
import techit.gongsimchae.domain.groupbuying.itemoption.dto.ItemOptionCreateDto;
import techit.gongsimchae.domain.groupbuying.itemoption.entity.ItemOption;
import techit.gongsimchae.domain.groupbuying.itemoption.repository.ItemOptionRepository;
import techit.gongsimchae.domain.groupbuying.itemoption.service.ItemOptionService;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderStatus;
import techit.gongsimchae.domain.groupbuying.orderitem.repository.OrderItemRepository;
import techit.gongsimchae.domain.groupbuying.orders.entity.Orders;
import techit.gongsimchae.domain.groupbuying.orders.repository.OrdersRepository;
import techit.gongsimchae.domain.groupbuying.reviews.entity.Review;
import techit.gongsimchae.domain.groupbuying.reviews.repository.ReviewRepository;
import techit.gongsimchae.global.dto.AccountDto;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    private final ImageS3Service imageS3Service;
    private final OrderItemRepository orderItemRepository;
    private final OrdersRepository ordersRepository;
    private final ImageFileRepository imageFileRepository;
    private final ReviewRepository reviewRepository;
    private final EventRepository eventRepository;
    private final ItemOptionRepository itemOptionRepository;


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
    public Item createItem(ItemCreateDto itemCreateDto, Long userId) {
        Category category = categoryRepository.findByName(itemCreateDto.getCategoryName())
                .orElseThrow(() -> {
                            throw new IllegalArgumentException("Category not found");
                        }
                );
        Item item = new Item(itemCreateDto, category);
        itemRepository.save(item);

        imageS3Service.storeFiles(itemCreateDto.getImages(), "images", item);

        if (itemCreateDto.getOptions() != null) {
            for(ItemOptionCreateDto optionCreateDto : itemCreateDto.getOptions()) {
                ItemOption itemOption = new ItemOption(item, optionCreateDto.getContent(), optionCreateDto.getPrice());
                itemOptionRepository.save(itemOption);
            }
        }
        return item;
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
    public List<ItemCardResDtoWeb> getRecentItems(){
        List<Item> items = itemRepository.findTop8ByOrderByCreateDateDesc();
        List<ItemCardResDtoWeb> itemCardResDtoWebs = new ArrayList<>();
        for (Item item : items) {
            ImageFile imageFile = imageFileRepository.findByItem(item);
            itemCardResDtoWebs.add(new ItemCardResDtoWeb(item, imageFile));
        }
        return itemCardResDtoWebs;
    }

    public List<ItemCardResDtoWeb> getPopularItems(){
        List<Item> items = itemRepository.findTop8ByOrderByGroupBuyingQuantityDesc();
        List<ItemCardResDtoWeb> itemCardResDtoWebs = new ArrayList<>();
        for (Item item : items) {
            ImageFile imageFile = imageFileRepository.findByItem(item);
            itemCardResDtoWebs.add(new ItemCardResDtoWeb(item, imageFile));
        }
        return itemCardResDtoWebs;
    }

    public List<Item> getItemsByCategory(Category category) {
        return itemRepository.findAllByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<ItemCardResDtoWeb> getItemsPageByCategory(Category category, Integer page, Integer per_page,
                                                          Integer sorted_type){
        SortType sortType = getInstanceByTypeNumber(sorted_type);
        Pageable pageable;
        if (sortType.equals(신상품순)){
            pageable = PageRequest.of(page, per_page, Sort.by(Direction.DESC,"createDate"));
        } else if (sortType.equals(판매량순)) {
            pageable = PageRequest.of(page, per_page, Sort.by(Direction.DESC, "cumulativeSalesVolume"));
        } else if (sortType.equals(리뷰많은순)){
            pageable = PageRequest.of(page, per_page, Sort.by(Direction.DESC, "reviewCount"));
        } else if (sortType.equals(낮은가격순)){
            pageable = PageRequest.of(page, per_page, Sort.by(Direction.ASC, "originalPrice"));
        } else if (sortType.equals(높은가격순)) {
            pageable = PageRequest.of(page, per_page, Sort.by(Direction.DESC, "originalPrice"));
        } else {
            throw new CustomWebException("존재하지 않는 정렬기준입니다.");
        }
        Page<Item> items = itemRepository.findAllByCategory(category, pageable);
        List<ItemCardResDtoWeb> itemCardResDtoWebs = new ArrayList<>();
        for (Item item : items) {
            ImageFile imageFile = imageFileRepository.findByItem(item);
            itemCardResDtoWebs.add(new ItemCardResDtoWeb(item, imageFile));
        }
        return itemCardResDtoWebs;
    }

    public ItemRespDtoWeb getItem(String id) {
        Item item = itemRepository.findByUID(id).orElseThrow(() -> new CustomWebException("not found item"));
        List<ImageFile> imageFiles = imageFileRepository.findAllByItem(item);
        List<String> imageUrls = imageFiles.stream().map(ImageFile::getStoreFilename).toList();
        return new ItemRespDtoWeb(item, imageUrls);
    }

    @Transactional(readOnly = true)
    public List<ItemCardResDtoWeb> getTop200NewItemsPage(Integer page, Integer per_page, Integer sorted_type) {
        SortType sortType = getInstanceByTypeNumber(sorted_type);
        Pageable pageable = PageRequest.of(page, per_page);
        Page<Item> newItemsPage;
        if (sortType.equals(신상품순)){
            newItemsPage = itemRepository.findTop200ByOrderByCreateDateDesc(pageable);
        } else if (sortType.equals(판매량순)) {
            newItemsPage = itemRepository.findTop200ByCreateDateAndSortByCumulativeSalesVolumeDesc(pageable);
        } else if (sortType.equals(리뷰많은순)){
            newItemsPage = itemRepository.findTop200ByCreateDateAndSortByReviewCountDesc(pageable);
        } else if (sortType.equals(낮은가격순)){
            newItemsPage = itemRepository.findTop200ByCreateDateAndSortByOriginalPriceAsc(pageable);
        } else if (sortType.equals(높은가격순)) {
            newItemsPage = itemRepository.findTop200ByCreateDateAndSortByOriginalPriceDesc(pageable);
        } else {
            throw new CustomWebException("존재하지 않는 정렬기준입니다.");
        }
        List<ItemCardResDtoWeb> itemCardResDtoWebs = new ArrayList<>();
        for (Item item : newItemsPage) {
            ImageFile imageFile = imageFileRepository.findByItem(item);
            itemCardResDtoWebs.add(new ItemCardResDtoWeb(item, imageFile));
        }
        return itemCardResDtoWebs;
    }

    @Transactional(readOnly = true)
    public List<ItemCardResDtoWeb> getTop200BestItemsPage(Integer page, Integer per_page, Integer sorted_type) {
        SortType sortType = getInstanceByTypeNumber(sorted_type);
        Pageable pageable = PageRequest.of(page, per_page);
        Page<Item> bestItemsPage;
        if (sortType.equals(신상품순)){
            bestItemsPage = itemRepository.findTop200ByCumulativeSalesVolumeAndSortByCreateDateDesc(pageable);
        } else if (sortType.equals(판매량순)) {
            bestItemsPage = itemRepository.findTop200ByOrderByCumulativeSalesVolumeDesc(pageable);
        } else if (sortType.equals(리뷰많은순)){
            bestItemsPage = itemRepository.findTop200ByCumulativeSalesVolumeAndSortByReviewCountDesc(pageable);
        } else if (sortType.equals(낮은가격순)){
            bestItemsPage = itemRepository.findTop200ByCumulativeSalesVolumeAndSortByOriginalPriceAsc(pageable);
        } else if (sortType.equals(높은가격순)) {
            bestItemsPage = itemRepository.findTop200ByCumulativeSalesVolumeAndSortByOriginalPriceDesc(pageable);
        } else {
            throw new CustomWebException("존재하지 않는 정렬기준입니다.");
        }
        List<ItemCardResDtoWeb> itemCardResDtoWebs = new ArrayList<>();
        for (Item item : bestItemsPage) {
            ImageFile imageFile = imageFileRepository.findByItem(item);
            itemCardResDtoWebs.add(new ItemCardResDtoWeb(item, imageFile));
        }
        return itemCardResDtoWebs;
    }

    public Page<ItemRespDtoWeb> searchItems(ItemSearchForm itemSearchForm, Pageable pageable) {
        return itemRepository.findItemsByKeyword(itemSearchForm, pageable);
    }

    public ReviewItemResDtoWeb getReviewItems(AccountDto accountDto) {
        List<Orders> orders = ordersRepository.findAllByUserIdAndOrderStatus(accountDto.getId(), OrderStatus.완료);
        List<OrderItem> ordersItems = orderItemRepository.findAllByOrdersIn(orders);
        List<Item> items = ordersItems.stream()
                .map((ordersItem) -> itemRepository.findById(ordersItem.getItem().getId()).orElseThrow(
                        () -> new CustomWebException(ErrorMessage.ITEM_NOT_FOUND)
                )).toList();
        List<Review> reviews = reviewRepository.findAllByUserId(accountDto.getId());

        List<ReviewAbleItemResDtoWeb> reviewAbleItemResDtoWebs = new ArrayList<>();
        List<ReviewedItemResDtoWeb> reviewedItemResDtoWebs = new ArrayList<>();
        for (Item item : items) {
            ImageFile imageFile = imageFileRepository.findByItem(item);
            Review matchingReview = reviews.stream()
                    .filter(review -> review.getItem().equals(item))
                    .findFirst()
                    .orElse(null);

            if (matchingReview != null) {
                reviewedItemResDtoWebs.add(new ReviewedItemResDtoWeb(item, imageFile.getStoreFilename(), matchingReview.getCreateDate()));
            } else {
                reviewAbleItemResDtoWebs.add(new ReviewAbleItemResDtoWeb(item, imageFile.getStoreFilename()));
            }
        }
        return new ReviewItemResDtoWeb(reviewAbleItemResDtoWebs, reviewedItemResDtoWebs);
    }

    public List<ItemCardResDtoWeb> getCategoriesItems(List<Category> categories, Integer page, Integer perPage, Integer sortedType) {
        List<Item> items = itemRepository.findAllByCategoryIn(categories);
        List<ItemCardResDtoWeb> itemCardResDtoWebs = new ArrayList<>();
        for (Item item : items) {
            ImageFile imageFile = imageFileRepository.findByItem(item);
            itemCardResDtoWebs.add(new ItemCardResDtoWeb(item, imageFile));
        }
        return itemCardResDtoWebs;
    }
}
