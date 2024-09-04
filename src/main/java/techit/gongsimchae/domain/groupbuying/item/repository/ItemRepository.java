package techit.gongsimchae.domain.groupbuying.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.item.entity.ItemStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository {
    List<Item> findTop8ByDeleteStatusOrderByCreateDateDesc(Integer deleteStatus);
    List<Item> findTop8ByDeleteStatusOrderByGroupBuyingQuantityDesc(Integer deleteStatus);
    List<Item> findAllByCategoryAndDeleteStatus(Category category, Integer deleteStatus);
    List<Item> findAllByCategoryInAndDeleteStatus(List<Category> categories, Integer deleteStatus);
    List<Item> findAllByDeleteStatus(Integer deleteStatus);
    Page<Item> findAllByCategoryAndDeleteStatus(Category category, Integer deleteStatus, Pageable pageable);
    Optional<Item> findByUIDAndDeleteStatus(String id,Integer deleteStatus);
    Page<Item> findTop200ByDeleteStatusOrderByCreateDateDesc(Integer deleteStatus, Pageable pageable);
    Page<Item> findTop200ByDeleteStatusOrderByCumulativeSalesVolumeDesc(Integer deleteStatus, Pageable pageable);
    boolean existsByCategoryAndItemStatus(Category category, ItemStatus itemStatus);


    /**
     * 신상품 - 신상품순
     */
    @Query(
            value = "SELECT * FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY create_date DESC LIMIT 200) sub",
            countQuery = "SELECT COUNT(*) FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY create_date DESC LIMIT 200) sub",
            nativeQuery = true
    )
    Page<Item> findTop200ByOrderByCreateDateDesc(Pageable pageable);


    /**
     * 신상품 - 판매량순
     */
    @Query(
            value = "SELECT * FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY create_date DESC LIMIT 200) sub ORDER BY cumulative_sales_volume DESC",
            countQuery = "SELECT COUNT(*) FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY create_date DESC LIMIT 200) sub",
            nativeQuery = true
    )
    Page<Item> findTop200ByCreateDateAndSortByCumulativeSalesVolumeDesc(Pageable pageable);

    /**
     * 신상품 - 리뷰많은순
     */
    @Query(
            value = "SELECT * FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY create_date DESC LIMIT 200) sub ORDER BY review_count DESC",
            countQuery = "SELECT COUNT(*) FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY create_date DESC LIMIT 200) sub",
            nativeQuery = true
    )
    Page<Item> findTop200ByCreateDateAndSortByReviewCountDesc(Pageable pageable);

    /**
     * 신상품 - 낮은가격순
     */
    @Query(
            value = "SELECT * FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY create_date DESC LIMIT 200) sub ORDER BY original_price ASC",
            countQuery = "SELECT COUNT(*) FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY create_date DESC LIMIT 200) sub",
            nativeQuery = true
    )
    Page<Item> findTop200ByCreateDateAndSortByOriginalPriceAsc(Pageable pageable);

    /**
     * 신상품 - 높은 가격순
     */
    @Query(
            value = "SELECT * FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY create_date DESC LIMIT 200) sub ORDER BY original_price DESC",
            countQuery = "SELECT COUNT(*) FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY create_date DESC LIMIT 200) sub",
            nativeQuery = true
    )
    Page<Item> findTop200ByCreateDateAndSortByOriginalPriceDesc(Pageable pageable);

    /**
     * 베스트 - 신상품순
     */
    @Query(
            value = "SELECT * FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY cumulative_sales_volume DESC LIMIT 200) sub ORDER BY create_date DESC",
            countQuery = "SELECT COUNT(*) FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY cumulative_sales_volume DESC LIMIT 200) sub",
            nativeQuery = true
    )
    Page<Item> findTop200ByCumulativeSalesVolumeAndSortByCreateDateDesc(Pageable pageable);

    /**
     * 베스트 - 판매량순
     */
    @Query(
            value = "SELECT * FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY cumulative_sales_volume DESC LIMIT 200) sub",
            countQuery = "SELECT COUNT(*) FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY cumulative_sales_volume DESC LIMIT 200) sub",
            nativeQuery = true
    )
    Page<Item> findTop200ByOrderByCumulativeSalesVolumeDesc(Pageable pageable);

    /**
     * 베스트 - 리뷰많은순
     */
    @Query(
            value = "SELECT * FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY cumulative_sales_volume DESC LIMIT 200) sub ORDER BY review_count DESC",
            countQuery = "SELECT COUNT(*) FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY cumulative_sales_volume DESC LIMIT 200) sub",
            nativeQuery = true
    )
    Page<Item> findTop200ByCumulativeSalesVolumeAndSortByReviewCountDesc(Pageable pageable);

    /**
     * 베스트 - 낮은가격순
     */
    @Query(
            value = "SELECT * FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY cumulative_sales_volume DESC LIMIT 200) sub ORDER BY original_price ASC",
            countQuery = "SELECT COUNT(*) FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY cumulative_sales_volume DESC LIMIT 200) sub",
            nativeQuery = true
    )
    Page<Item> findTop200ByCumulativeSalesVolumeAndSortByOriginalPriceAsc(Pageable pageable);

    /**
     * 베스트 - 높은 가격순
     */
    @Query(
            value = "SELECT * FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY cumulative_sales_volume DESC LIMIT 200) sub ORDER BY original_price DESC",
            countQuery = "SELECT COUNT(*) FROM (SELECT * FROM item WHERE delete_status = 0 ORDER BY cumulative_sales_volume DESC LIMIT 200) sub",
            nativeQuery = true
    )
    Page<Item> findTop200ByCumulativeSalesVolumeAndSortByOriginalPriceDesc(Pageable pageable);

    // ------------------------------------------ 이벤트 아이템 ------------------------------------------------
    /**
     * 이벤트 - 신상품순
     */
    Page<Item> findAllByCategoryInOrderByCreateDateDesc(List<Category> categories, Pageable pageable);

    /**
     * 이벤트 - 판매량순
     */
    Page<Item> findAllByCategoryInOrderByCumulativeSalesVolumeDesc(List<Category> categories, Pageable pageable);

    /**
     * 이벤트 - 리뷰많은순
     */
    Page<Item> findAllByCategoryInOrderByReviewCount(List<Category> categories, Pageable pageable);

    /**
     * 이벤트 - 낮은가격순
     */
    Page<Item> findAllByCategoryInOrderByOriginalPriceAsc(List<Category> categories, Pageable pageable);

    /**
     * 이벤트 - 높은 가격순
     */
    Page<Item> findAllByCategoryInOrderByOriginalPriceDesc(List<Category> categories, Pageable pageable);

    /**
     * 스케줄러 - 해당 ItemStatus를 가진 아이템 리스트 반환
     */
    List<Item> findAllByItemStatusAndGroupBuyingLimitTimeBefore(ItemStatus itemStatus, LocalDateTime groupBuyingLimitTime);
}
