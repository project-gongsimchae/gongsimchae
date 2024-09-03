package techit.gongsimchae.domain.groupbuying.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.es.repository.ItemElasticRepository;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.imagefile.entity.ItemImageFileStatus;
import techit.gongsimchae.domain.common.imagefile.repository.ImageFileRepository;
import techit.gongsimchae.domain.common.imagefile.service.ImageS3Service;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.category.repository.CategoryRepository;
import techit.gongsimchae.domain.groupbuying.event.repository.EventRepository;
import techit.gongsimchae.domain.groupbuying.item.dto.*;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.item.entity.ItemStatus;
import techit.gongsimchae.domain.groupbuying.item.entity.SortType;
import techit.gongsimchae.domain.groupbuying.item.repository.ItemRepository;
import techit.gongsimchae.domain.groupbuying.itemoption.dto.ItemOptionCreateDto;
import techit.gongsimchae.domain.groupbuying.itemoption.dto.ItemOptionUpdateDto;
import techit.gongsimchae.domain.groupbuying.itemoption.entity.ItemOption;
import techit.gongsimchae.domain.groupbuying.itemoption.repository.ItemOptionRepository;
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

import java.util.ArrayList;
import java.util.List;

import static techit.gongsimchae.domain.groupbuying.item.entity.SortType.*;

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
    private final ItemElasticRepository itemElasticRepository;

    public void save(Item item) {
        itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }


    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .filter(item -> item.getDeleteStatus() == 0)
                .orElse(null);
    }


    @Transactional
    public Item createItem(ItemCreateDto itemCreateDto, Long userId) {
        Category category = categoryRepository.findByName(itemCreateDto.getCategoryName())
                .orElseThrow(() -> new CustomWebException(ErrorMessage.CATEGORY_NOT_FOUND));
        Item item = new Item(itemCreateDto, category);
        Item savedItem = itemRepository.save(item);

        List<ImageFile> imageFiles = imageS3Service.storeFiles(itemCreateDto.getImages(), "images", item);
        List<ImageFile> detailImageFiles = imageS3Service.storeFiles(itemCreateDto.getDetailImages(), "images", item, ItemImageFileStatus.DETAIL);

        createItemDocument(savedItem, imageFiles);

        if (itemCreateDto.getOptions() != null) {
            for(ItemOptionCreateDto optionCreateDto : itemCreateDto.getOptions()) {
                ItemOption itemOption = new ItemOption(item, optionCreateDto.getContent(), optionCreateDto.getPrice());
                itemOptionRepository.save(itemOption);
            }
        }
        return item;
    }

    private void createItemDocument(Item item, List<ImageFile> imageFiles) {
        String url = null;
        if(!imageFiles.isEmpty()) {
            url = imageFiles.get(0).getStoreFilename();
        }
        itemElasticRepository.createItemDocument(item, url);
    }

    @Transactional
    public Item updateItem(Long id, ItemUpdateDto itemUpdateDto) {
        Item item = itemRepository.findById(id).filter(i -> i.getDeleteStatus() == 0)
                .orElseThrow(() -> new CustomWebException(ErrorMessage.ITEM_NOT_FOUND));

        Category category = categoryRepository.findByName(itemUpdateDto.getCategoryName())
                .orElseThrow(() -> new CustomWebException(ErrorMessage.CATEGORY_NOT_FOUND));

        item.UpdateDto(itemUpdateDto, category);

        // 새로운 이미지 파일 저장
        imageS3Service.storeFiles(itemUpdateDto.getImages(), "images", item, ItemImageFileStatus.THUMBNAIL);
        imageS3Service.storeFiles(itemUpdateDto.getDetailImages(), "images", item, ItemImageFileStatus.DETAIL);

        // 삭제할 이미지 파일 처리
        if (itemUpdateDto.getDeleteImages() != null && !itemUpdateDto.getDeleteImages().isEmpty()) {
            for (Long deleteImageId : itemUpdateDto.getDeleteImages()) {
                ImageFile imageFile = imageFileRepository.findById(deleteImageId)
                        .orElseThrow(() -> new CustomWebException(ErrorMessage.IMAGE_NOT_FOUND));

                imageS3Service.deleteFile(imageFile.getStoreFilename());
                imageS3Service.deleteFileFromDb(imageFile);
            }
        }

        // 아이템에 연결된 기존 옵션들을 가져옴
        List<ItemOption> existingOptions = itemOptionRepository.findAllByItemId(item.getId());

        // 모든 옵션을 업데이트하고 새 옵션 추가
        for (ItemOptionUpdateDto optionUpdateDto : itemUpdateDto.getOptions()) {
            ItemOption existingOption = existingOptions.stream()
                    .filter(option -> option.getId().equals(optionUpdateDto.getId()))
                    .findFirst()
                    .orElseGet(() -> {
                        ItemOption newOption = new ItemOption(item, optionUpdateDto.getContent(), optionUpdateDto.getPrice());
                        itemOptionRepository.save(newOption);
                        return newOption;
                    });

            existingOption.updateOption(optionUpdateDto.getContent(), optionUpdateDto.getPrice());
        }

        // 기존 옵션 중 업데이트 목록에 없는 옵션 삭제
        List<Long> updatedOptionIds = itemUpdateDto.getOptions().stream()
                .map(ItemOptionUpdateDto::getId)
                .toList();
        existingOptions.stream()
                .filter(option -> !updatedOptionIds.contains(option.getId()))
                .forEach(itemOptionRepository::delete);

        return itemRepository.save(item);
    }

    @Transactional
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new CustomWebException(ErrorMessage.ITEM_NOT_FOUND));
        item.markAsDeleted();

    }

    @Transactional
    public void restoreItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new CustomWebException(ErrorMessage.ITEM_NOT_FOUND));
        item.restore();
    }


    /**
     * 최신등록 8개 아이템
     * 참여가 많은 8개 아이템을 리스트형태로 반환하는 메서드입니다. **/
    public List<ItemCardResDtoWeb> getRecentItems(){
        List<Item> items = itemRepository.findTop8ByDeleteStatusOrderByCreateDateDesc(0);
        List<ItemCardResDtoWeb> itemCardResDtoWebs = new ArrayList<>();
        for (Item item : items) {
            ImageFile imageFile = imageFileRepository.findAllByItem(item).get(0);
            itemCardResDtoWebs.add(new ItemCardResDtoWeb(item, imageFile));
        }
        return itemCardResDtoWebs;
    }

    public List<ItemCardResDtoWeb> getPopularItems(){
        List<Item> items = itemRepository.findTop8ByDeleteStatusOrderByGroupBuyingQuantityDesc(0);
        List<ItemCardResDtoWeb> itemCardResDtoWebs = new ArrayList<>();
        for (Item item : items) {
            ImageFile imageFile = imageFileRepository.findAllByItem(item).get(0);
            itemCardResDtoWebs.add(new ItemCardResDtoWeb(item, imageFile));
        }
        return itemCardResDtoWebs;
    }

    public List<Item> getItemsByCategory(Category category) {
        return itemRepository.findAllByCategoryAndDeleteStatus(category, 0);
    }

    /**
     * 카테고리 선택 페이지 반환
     */
    @Transactional(readOnly = true)
    public Page<ItemCardResDtoWeb> getItemsPageByCategory(Category category, Integer page, Integer per_page,
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
            throw new CustomWebException(ErrorMessage.SORT_TYPE_NOR_FOUND);
        }
        Page<Item> items = itemRepository.findAllByCategoryAndDeleteStatus(category, 0, pageable);
        return items.map(item -> {
            ImageFile imageFile = imageFileRepository.findAllByItem(item).get(0);
            return new ItemCardResDtoWeb(item, imageFile);
        });
    }

    public ItemRespDtoWeb getItem(String id) {
        Item item = itemRepository.findByUIDAndDeleteStatus(id, 0).orElseThrow(() -> new CustomWebException("not found item"));
        List<ImageFile> imageFiles = imageFileRepository.findAllByItem(item);
        List<String> imageUrls = imageFiles.stream().map(ImageFile::getStoreFilename).toList();
        return new ItemRespDtoWeb(item, imageUrls);
    }

    @Transactional(readOnly = true)
    public Page<ItemCardResDtoWeb> getTop200NewItemsPage(Integer page, Integer per_page, Integer sorted_type) {
        SortType sortType = getInstanceByTypeNumber(sorted_type);
        Pageable pageable = PageRequest.of(page, per_page);
        Page<Item> newItemsPage;
        if (sortType.equals(신상품순)){
            newItemsPage = itemRepository.findTop200ByDeleteStatusOrderByCreateDateDesc(0, pageable);
        } else if (sortType.equals(판매량순)) {
            newItemsPage = itemRepository.findTop200ByCreateDateAndSortByCumulativeSalesVolumeDesc(pageable);
        } else if (sortType.equals(리뷰많은순)){
            newItemsPage = itemRepository.findTop200ByCreateDateAndSortByReviewCountDesc(pageable);
        } else if (sortType.equals(낮은가격순)){
            newItemsPage = itemRepository.findTop200ByCreateDateAndSortByOriginalPriceAsc(pageable);
        } else if (sortType.equals(높은가격순)) {
            newItemsPage = itemRepository.findTop200ByCreateDateAndSortByOriginalPriceDesc(pageable);
        } else {
            throw new CustomWebException(ErrorMessage.SORT_TYPE_NOR_FOUND);
        }
        return newItemsPage.map(item -> {
            ImageFile imageFile = imageFileRepository.findAllByItem(item).get(0);
            return new ItemCardResDtoWeb(item, imageFile);
        });
    }

    @Transactional(readOnly = true)
    public Page<ItemCardResDtoWeb> getTop200BestItemsPage(Integer page, Integer per_page, Integer sorted_type) {
        SortType sortType = getInstanceByTypeNumber(sorted_type);
        Pageable pageable = PageRequest.of(page, per_page);
        Page<Item> bestItemsPage;
        if (sortType.equals(신상품순)){
            bestItemsPage = itemRepository.findTop200ByCumulativeSalesVolumeAndSortByCreateDateDesc(pageable);
        } else if (sortType.equals(판매량순)) {
            bestItemsPage = itemRepository.findTop200ByDeleteStatusOrderByCumulativeSalesVolumeDesc(0, pageable);
        } else if (sortType.equals(리뷰많은순)){
            bestItemsPage = itemRepository.findTop200ByCumulativeSalesVolumeAndSortByReviewCountDesc(pageable);
        } else if (sortType.equals(낮은가격순)){
            bestItemsPage = itemRepository.findTop200ByCumulativeSalesVolumeAndSortByOriginalPriceAsc(pageable);
        } else if (sortType.equals(높은가격순)) {
            bestItemsPage = itemRepository.findTop200ByCumulativeSalesVolumeAndSortByOriginalPriceDesc(pageable);
        } else {
            throw new CustomWebException(ErrorMessage.SORT_TYPE_NOT_FOUND);
        }
        return bestItemsPage.map(item -> {
            ImageFile imageFile = imageFileRepository.findAllByItem(item).get(0);
            return new ItemCardResDtoWeb(item, imageFile);
        });
    }

    public Page<ItemCardResDtoWeb> getCategoriesItems(List<Category> categories, Integer page, Integer per_page, Integer sorted_type) {
        SortType sortType = getInstanceByTypeNumber(sorted_type);
        Pageable pageable = PageRequest.of(page, per_page);
        Page<Item> categoriesItemsPage;
        if (sortType.equals(신상품순)){
            categoriesItemsPage = itemRepository.findAllByCategoryInOrderByCreateDateDesc(categories, pageable);
        } else if (sortType.equals(판매량순)) {
            categoriesItemsPage = itemRepository.findAllByCategoryInOrderByCumulativeSalesVolumeDesc(categories, pageable);
        } else if (sortType.equals(리뷰많은순)){
            categoriesItemsPage = itemRepository.findAllByCategoryInOrderByReviewCount(categories, pageable);
        } else if (sortType.equals(낮은가격순)){
            categoriesItemsPage = itemRepository.findAllByCategoryInOrderByOriginalPriceAsc(categories, pageable);
        } else if (sortType.equals(높은가격순)) {
            categoriesItemsPage = itemRepository.findAllByCategoryInOrderByOriginalPriceDesc(categories, pageable);
        } else {
            throw new CustomWebException(ErrorMessage.SORT_TYPE_NOR_FOUND);
        }
        return categoriesItemsPage.map(item -> {
            ImageFile imageFile = imageFileRepository.findAllByItem(item).get(0);
            return new ItemCardResDtoWeb(item, imageFile);
        });
    }

    public Page<ItemRespDtoWeb> searchItems(ItemSearchForm itemSearchForm, Pageable pageable) {
        return itemRepository.findItemsByKeyword(itemSearchForm, pageable);
    }

    public ReviewItemResDtoWeb getReviewItems(AccountDto accountDto) {
        List<Orders> orders = ordersRepository.findAllByUserIdAndOrderStatus(accountDto.getId(), OrderStatus.완료);
        List<OrderItem> ordersItems = orderItemRepository.findAllByOrdersIn(orders);
        List<Item> items = ordersItems.stream()
                .map((ordersItem) -> itemRepository.findById(ordersItem.getItemOption().getItem().getId()).orElseThrow(
                        () -> new CustomWebException(ErrorMessage.ITEM_NOT_FOUND)
                )).toList();
        List<Review> reviews = reviewRepository.findAllByUserId(accountDto.getId());

        List<ReviewAbleItemResDtoWeb> reviewAbleItemResDtoWebs = new ArrayList<>();
        List<ReviewedItemResDtoWeb> reviewedItemResDtoWebs = new ArrayList<>();
        for (Item item : items) {
            ImageFile imageFile = imageFileRepository.findAllByItem(item).get(0);
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

    /**
     * 해당 아이템의 ItemStatus를 바꾸는 메서드 입니다.
     *
     * @param itemId
     */
    @Transactional
    public void changeItemStatus(Long itemId, ItemStatus itemStatus) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new CustomWebException(ErrorMessage.ITEM_NOT_FOUND));
        item.updateItemStatus(itemStatus);
    }

}
